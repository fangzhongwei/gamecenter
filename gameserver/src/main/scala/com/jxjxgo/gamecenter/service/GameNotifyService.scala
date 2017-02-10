package com.jxjxgo.gamecenter.service

import java.sql.Timestamp
import javax.inject.Inject

import akka.actor.{ActorRef, ActorSystem, Props}
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.gamecenter.enumnate.GameStatus
import com.jxjxgo.gamecenter.helper.CardsHelper
import com.jxjxgo.gamecenter.repo.{TowVsOneRepository, TmGameRow, TmSeatRow}
import com.jxjxgo.memberber.rpc.domain.{MemberEndpoint, MemberResponse}
import com.twitter.util.{Await, Future}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.StringBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

/**
  * Created by fangzhongwei on 2016/12/20.
  */
trait GameNotifyService {
  def getOnlineMemberAkkaAddress(memberId: Long): Option[String]

  def createGame(traceId: String, gameType: Int, seat1: TmSeatRow, seat2: TmSeatRow, seat3: TmSeatRow)
}

class GameNotifyServiceImpl @Inject()(coordinateRepository: TowVsOneRepository, redisClientTemplate: RedisClientTemplate, memberClientService: MemberEndpoint[Future]) extends GameNotifyService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private[this] val akkaAddressPre = "gc.ddz.mi-addr:"
  private[this] var actor: ActorRef = _

  implicit def gameStatus2Byte(gameStatus: GameStatus): Short = gameStatus.getCode

  def notifyGameStart(traceId: String, memberId: Long, game: TmGameRow, gameIndex: Int, cards: List[Int], dzCards: List[Int], previousUsername: String, nextUsername: String): Unit = {

  }

  override def createGame(traceId: String, gameType: Int, seat1: TmSeatRow, seat2: TmSeatRow, seat3: TmSeatRow): Unit = {
    val memberId1 = seat1.memberId
    val memberId2 = seat2.memberId
    val memberId3 = seat3.memberId

    val (player1CardsList, player2CardsList, player3CardsList, dzCardsList) = CardsHelper.initCards()
    val gameId: Long = coordinateRepository.getNextGameId()
    val now = new Timestamp(System.currentTimeMillis())
    val cards1: String = player1CardsList.mkString(",")
    val cards2: String = player2CardsList.mkString(",")
    val cards3: String = player3CardsList.mkString(",")
    val dzCards: String = dzCardsList.mkString(",")
    val game: TmGameRow = TmGameRow(gameId, gameType, GameStatus.Playing.getCode, memberId1, memberId2, memberId3, cards1, cards2, cards3, dzCards, 1, now, now)
    coordinateRepository.createGame(game)

    val memberResponse1: MemberResponse = Await.result(memberClientService.getMemberById(traceId, memberId1))
    val memberResponse2: MemberResponse = Await.result(memberClientService.getMemberById(traceId, memberId2))
    val memberResponse3: MemberResponse = Await.result(memberClientService.getMemberById(traceId, memberId3))

    notifyGameStart(seat1.traceId, memberId1, game, 1, player1CardsList, dzCardsList, memberResponse3.nickName, memberResponse2.nickName)
    notifyGameStart(seat2.traceId, memberId2, game, 2, player2CardsList, dzCardsList, memberResponse1.nickName, memberResponse3.nickName)
    notifyGameStart(seat3.traceId, memberId3, game, 3, player3CardsList, dzCardsList, memberResponse2.nickName, memberResponse1.nickName)
  }

  override def getOnlineMemberAkkaAddress(memberId: Long): Option[String] = {
    redisClientTemplate.getString(new StringBuilder(akkaAddressPre).append(memberId).toString)
  }
}
