package com.lawsofnature.gamecenter.service

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}
import javax.inject.Inject

import RpcSSO.SessionResponse
import akka.actor.{Actor, ActorSystem, Props}
import akka.io.Tcp.Write
import akka.util.CompactByteString
import com.google.common.cache.{Cache, CacheBuilder}
import com.lawsofnature.common.rabbitmq.RabbitmqProducerTemplate
import com.lawsofnature.common.redis.RedisClientTemplate
import com.lawsofnature.gamecenter.common.domain.GameCenterRequest.JoinRequest
import com.lawsofnature.gamecenter.common.domain.GameCenterRequest.JoinRequest.Builder
import com.lawsofnature.gamecenter.common.domain.SocketResponse.GameStartResponse
import com.lawsofnature.gamecenter.common.domain.{GameCenterRequest, SocketResponse}
import com.lawsofnature.gamecenter.domain.GameTable
import com.lawsofnature.gamecenter.enumnate.{GameType, OnlineMemberGameStatus, SocketResponseType}
import com.lawsofnature.gamecenter.helper.SocketDataGenerator
import com.lawsofnature.gamecenter.noti.GameStart
import com.lawsofnature.gamecenter.server.TCPHandler
import com.lawsofnature.sso.client.SSOClientService
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/12/16.
  */
trait GameService {
  def initDispatchActor: Future[Unit]

  def playCards(traceId: String, token: String, cards: String): Future[Unit]

  def login(traceId: String, token: String, tcpHandler: TCPHandler): Future[Option[Long]]

  def join(traceId: String, token: String, gameType: GameType): Future[GameTable]

  def getHandler(memberId: Long): TCPHandler
}

class DispatchActor(gameService: GameService) extends Actor {
  override def receive: Receive = {
    case GameStart(traceId, memberId, cards, dzCards, isChooseDz, previousUsername, nextUsername) =>
      val builder: GameStartResponse.Builder = SocketResponse.GameStartResponse.newBuilder()
      builder.setTi(traceId)
      cards.zipWithIndex.foreach {
        card => builder.setCards(card._2, card._1)
      }
      dzCards.zipWithIndex.foreach {
        card => builder.setDzCards(card._2, card._1)
      }
      builder.setIsChooseDz(isChooseDz)
      builder.setPreviousUsername(previousUsername)
      builder.setNextUsername(nextUsername)
      gameService.getHandler(memberId).sender() ! Write(CompactByteString(SocketDataGenerator.generate(SocketResponseType.GameStart.getCode, builder.build().toByteArray, "ABCD1234")))
  }
}

class GameServiceImpl @Inject()(ssoClientService: SSOClientService, redisClientTemplate: RedisClientTemplate, rabbitmqProducerTemplate: RabbitmqProducerTemplate) extends GameService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)
  private[this] val tokenSessionMap = new ConcurrentHashMap[String, SessionResponse]
  private[this] val memberIdHandlerMap = new ConcurrentHashMap[Long, TCPHandler]

  private[this] val tokenSessionJvmCache: Cache[String, SessionResponse] = CacheBuilder.newBuilder().expireAfterAccess(24, TimeUnit.HOURS).maximumSize(1000000).build().asInstanceOf[Cache[String, SessionResponse]]
  private[this] val memberIdHandlerJvmCache: Cache[Long, TCPHandler] = CacheBuilder.newBuilder().expireAfterAccess(24, TimeUnit.HOURS).maximumSize(1000000).build().asInstanceOf[Cache[Long, TCPHandler]]

  // game center, ddz , key:memberId, value:game status
  private[this] val gameStatusPre = "gc.ddz.mi-gs:"
  private[this] val gameStatusExpireSeconds = 24 * 60 * 60
  // game center, ddz , key:memberId, value:akka address
  private[this] val akkaAddressPre = "gc.ddz.mi-addr:"
  private[this] val akkaAddressExpireSeconds = 24 * 60 * 60

  private[this] var akkaHostname: String = ""
  private[this] var akkaPort: Int = 0

  override def initDispatchActor: Future[Unit] = {
    val promise: Promise[Unit] = Promise[Unit]()
    Future {
      val config: Config = ConfigFactory.load("dispatch.system.akka.config")
      val system = ActorSystem("DispatchSystem", config)
      system.actorOf(Props(classOf[DispatchActor], this), "Dispatcher")
      akkaHostname = config.getString("remote.netty.tcp.hostname")
      akkaPort = config.getInt("remote.netty.tcp.port")
      logger.info(s"Dispatcher startup, config:$config")
      promise.success()
    }
    promise.future
  }

  def setOnlineMemberChannelAddress(memberId: Long, akkaHostname: String, akkaPort: Int) = {
    redisClientTemplate.setString(new StringBuilder(akkaAddressPre).append(memberId).toString, new StringBuilder(akkaHostname).append(":").append(akkaPort).toString, akkaAddressExpireSeconds)
  }

  override def login(traceId: String, token: String, tcpHandler: TCPHandler): Future[Option[Long]] = {
    val promise: Promise[Option[Long]] = Promise[Option[Long]]()
    Future {
      val sessionResponse: SessionResponse = ssoClientService.touch(traceId, token)
      sessionResponse.success match {
        case true =>
          tokenSessionMap.put(token, sessionResponse)
          memberIdHandlerMap.put(sessionResponse.memberId, tcpHandler)
          setOnlineMemberGameStatus(sessionResponse.memberId, OnlineMemberGameStatus.Idel)
          setOnlineMemberChannelAddress(sessionResponse.memberId, akkaHostname, akkaPort)
          promise.success(Some(sessionResponse.memberId))
        case false =>
          promise.success(None)
      }
    }
    promise.future
  }

  def setOnlineMemberGameStatus(memberId: Long, status: OnlineMemberGameStatus): Unit = {
    redisClientTemplate.setString(new StringBuilder(gameStatusPre).append(memberId).toString, status.toString, gameStatusExpireSeconds)
  }

  def getOnlineMemberGameStatus(memberId: Long): OnlineMemberGameStatus = {
    redisClientTemplate.getString(new StringBuilder(gameStatusPre).append(memberId).toString) match {
      case Some(string) => (string == null || "".equals(string)) match {
        case true => OnlineMemberGameStatus.Idel
        case false => OnlineMemberGameStatus.valueOf(string)
      }
      case None => OnlineMemberGameStatus.Idel
    }
  }

  override def join(traceId: String, token: String, gameType: GameType): Future[GameTable] = {
    //        tcpHandler.sender() ! Write(CompactByteString("1".getBytes()))
    val promise: Promise[GameTable] = Promise[GameTable]()
    Future {
      val sessionResponse: SessionResponse = ssoClientService.touch(traceId, token)
      val waitingStatus: OnlineMemberGameStatus = OnlineMemberGameStatus.get(gameType, false)
      setOnlineMemberGameStatus(sessionResponse.memberId, waitingStatus)
      sendJoinMessage(traceId, sessionResponse.memberId, gameType)
    }
    promise.future
  }

  def sendJoinMessage(traceId: String, memberId: Long, gameType: GameType): Unit = {
    val builder: Builder = GameCenterRequest.JoinRequest.newBuilder()
    builder.setTraceId(traceId)
    builder.setMemberId(memberId)
    builder.setGameType(gameType.getCode)
    val joinRequest: JoinRequest = builder.build()
    val exchange: String = new StringBuilder(ConfigFactory.load().getString("game.mq.exchange")).append(gameType.getCode).toString()
    val exchangeType: String = new StringBuilder(ConfigFactory.load().getString("game.mq.exchangeType")).toString()
    val queue: String = new StringBuilder(ConfigFactory.load().getString("game.mq.queue")).append(gameType.getCode).toString()
    val routingKey: String = new StringBuilder(ConfigFactory.load().getString("game.mq.routingKey")).append(gameType.getCode).toString()

    rabbitmqProducerTemplate.send(exchange, exchangeType, queue, routingKey, joinRequest.toByteArray)
  }

  override def playCards(traceId: String, token: String, cards: String): Future[Unit] = {
    val promise: Promise[Unit] = Promise[Unit]()
    Future {
      val sessionResponse: SessionResponse = ssoClientService.touch(traceId, token)
    }
    promise.future
  }

  override def getHandler(memberId: Long): TCPHandler = memberIdHandlerMap.get(memberId)
}
