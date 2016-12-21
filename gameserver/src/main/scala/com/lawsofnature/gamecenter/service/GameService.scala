package com.lawsofnature.gamecenter.service

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

import RpcSSO.SessionResponse
import com.lawsofnature.common.rabbitmq.RabbitmqProducerTemplate
import com.lawsofnature.common.redis.RedisClientTemplate
import com.lawsofnature.gamecenter.common.domain.GameCenterRequest
import com.lawsofnature.gamecenter.common.domain.GameCenterRequest.JoinRequest
import com.lawsofnature.gamecenter.common.domain.GameCenterRequest.JoinRequest.Builder
import com.lawsofnature.gamecenter.domain.GameTable
import com.lawsofnature.gamecenter.enumnate.{GameType, OnlineMemberGameStatus}
import com.lawsofnature.gamecenter.server.TCPHandler
import com.lawsofnature.sso.client.SSOClientService
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/12/16.
  */
trait GameService {
  def playCards(traceId: String, token: String, cards: String): Future[Unit]

  def login(traceId: String, token: String, tcpHandler: TCPHandler): Future[Option[Long]]

  def join(traceId: String, token: String, gameType: GameType): Future[GameTable]
}

class GameServiceImpl @Inject()(ssoClientService: SSOClientService, redisClientTemplate: RedisClientTemplate, rabbitmqProducerTemplate: RabbitmqProducerTemplate) extends GameService {
  val tokenSessionMap = new ConcurrentHashMap[String, SessionResponse]
  val memberIdHandlerMap = new ConcurrentHashMap[Long, TCPHandler]
  // game center, ddz , key:memberId, value:game status
  val gameStatusPre = "gc.ddz.mi-gs:"
  val gameStatusExpireSeconds = 24 * 60 * 60

  override def login(traceId: String, token: String, tcpHandler: TCPHandler): Future[Option[Long]] = {
    val promise: Promise[Option[Long]] = Promise[Option[Long]]()
    Future {
      val sessionResponse: SessionResponse = ssoClientService.touch(traceId, token)
      sessionResponse.success match {
        case true =>
          tokenSessionMap.put(token, sessionResponse)
          memberIdHandlerMap.put(sessionResponse.memberId, tcpHandler)
          setOnlineMemberGameStatus(sessionResponse.memberId, OnlineMemberGameStatus.Idel)
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

  def sendJoinMessage(traceId:String, memberId: Long, gameType: GameType): Unit = {
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
}
