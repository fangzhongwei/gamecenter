package com.jxjxgo.gamecenter.service

import java.sql.Timestamp
import javax.inject.Inject

import akka.actor.ActorRef
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.gamecenter.domain.Seat
import com.jxjxgo.gamecenter.enumnate.GameStatus
import com.jxjxgo.gamecenter.helper.CardsHelper
import com.jxjxgo.gamecenter.repo.TowVsOneRepository
import com.jxjxgo.memberber.rpc.domain.{MemberEndpoint, MemberResponse}
import com.twitter.util.{Await, Future}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.StringBuilder

/**
  * Created by fangzhongwei on 2016/12/20.
  */
trait GameNotifyService {
  def getOnlineMemberAkkaAddress(memberId: Long): Option[String]

  def createGame(traceId: String, gameType: Int, seat1: Seat, seat2: Seat, seat3: Seat)
}

class GameNotifyServiceImpl @Inject()(towVsOneRepository: TowVsOneRepository, redisClientTemplate: RedisClientTemplate, memberClientService: MemberEndpoint[Future]) extends GameNotifyService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private[this] val akkaAddressPre = "gc.ddz.mi-addr:"
  private[this] var actor: ActorRef = _

  implicit def gameStatus2Byte(gameStatus: GameStatus): Short = gameStatus.getCode

  def notifyGameStart(traceId: String, seat: Seat, game: towVsOneRepository.TmGameRow, gameIndex: Int, cards: List[Int], dzCards: List[Int], previousNickName: String, nextNickName: String): Unit = {

  }

  override def createGame(traceId: String, gameType: Int, seat1: Seat, seat2: Seat, seat3: Seat): Unit = {


//    notifyGameStart(seat1.traceId, seat1, game, 1, player1CardsList, dzCardsList, memberResponse3.nickName, memberResponse2.nickName)
//    notifyGameStart(seat2.traceId, seat2, game, 2, player2CardsList, dzCardsList, memberResponse1.nickName, memberResponse3.nickName)
//    notifyGameStart(seat3.traceId, seat3, game, 3, player3CardsList, dzCardsList, memberResponse2.nickName, memberResponse1.nickName)
  }

  override def getOnlineMemberAkkaAddress(memberId: Long): Option[String] = {
    redisClientTemplate.getString(new StringBuilder(akkaAddressPre).append(memberId).toString)
  }
}
