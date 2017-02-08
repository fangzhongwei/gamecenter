package com.jxjxgo.gamecenter.service

import javax.inject.Inject

import com.jxjxgo.common.rabbitmq.RabbitmqProducerTemplate
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.game.rpc.domain._
import com.jxjxgo.gamecenter.enumnate.OnlineMemberGameStatus
import com.jxjxgo.sso.rpc.domain.SSOServiceEndpoint
import com.twitter.util.Future
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/16.
  */
trait GameService {
  def checkGameStatus(traceId: String, request: CheckGameStatusRequest): CheckGameStatusResponse

  def joinGame(traceId: String, request: JoinGameRequest): JoinGameResponse

  def playCards(traceId: String, request: PlayCardsRequest): PlayCardsResponse

  def setGameStatus(traceId: String, memberId: Long, gameStatus: String): GameBaseResponse
}

class GameServiceImpl @Inject()(ssoClientService: SSOServiceEndpoint[Future], redisClientTemplate: RedisClientTemplate, rabbitmqProducerTemplate: RabbitmqProducerTemplate) extends GameService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  // game center, ddz , key:memberId, value:game status
  private[this] val gameStatusPre = "gc.ddz.mi-gs:"
  private[this] val gameStatusExpireSeconds = 24 * 60 * 60
  // game center, ddz , key:memberId, value:akka address
  private[this] val akkaAddressPre = "gc.ddz.mi-addr:"
  private[this] val akkaAddressExpireSeconds = 24 * 60 * 60

  def setOnlineMemberChannelAddress(memberId: Long, akkaHostname: String, akkaPort: Int) = {
    redisClientTemplate.setString(new StringBuilder(akkaAddressPre).append(memberId).toString, new StringBuilder(akkaHostname).append(":").append(akkaPort).toString, akkaAddressExpireSeconds)
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

  override def checkGameStatus(traceId: String, request: CheckGameStatusRequest): CheckGameStatusResponse = {
    val onlineMemberGameStatus: OnlineMemberGameStatus = getOnlineMemberGameStatus(request.memberId)
    onlineMemberGameStatus match {
      case OnlineMemberGameStatus.Idel =>
      case _ =>
    }
    null
  }

  override def joinGame(traceId: String, request: JoinGameRequest): JoinGameResponse = {
    null
  }

  override def playCards(traceId: String, request: PlayCardsRequest): PlayCardsResponse = {
    null
  }

  override def setGameStatus(traceId: String, memberId: Long, gameStatus: String): GameBaseResponse = {
    null
  }
}
