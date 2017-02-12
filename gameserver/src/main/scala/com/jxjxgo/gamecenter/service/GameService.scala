package com.jxjxgo.gamecenter.service

import java.sql.Timestamp
import javax.inject.Inject

import com.jxjxgo.account.rpc.domain.{AccountEndpoint, DiamondAccountResponse}
import com.jxjxgo.common.exception.ErrorCode
import com.jxjxgo.common.kafka.template.ProducerTemplate
import com.jxjxgo.gamecenter.domain.mq.joingame.JoinGameMessage
import com.jxjxgo.gamecenter.enumnate.{GameStatus, SeatStatus}
import com.jxjxgo.gamecenter.repo.TowVsOneRepository
import com.jxjxgo.gamecenter.rpc.domain._
import com.jxjxgo.sso.rpc.domain.SSOServiceEndpoint
import com.twitter.util.{Await, Future}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/16.
  */
trait GameService {
  def playerOffline(traceId: String, socketUuid: Long, memberId: Long): GameBaseResponse

  def playerOnline(traceId: String, request: OnlineRequest): GameBaseResponse

  def generateSocketId(traceId: String): GenerateSocketIdResponse

  def checkGameStatus(traceId: String, request: CheckGameStatusRequest): CheckGameStatusResponse

  def joinGame(traceId: String, request: JoinGameRequest): GameBaseResponse

  def playCards(traceId: String, request: PlayCardsRequest): PlayCardsResponse
}

class GameServiceImpl @Inject()(ssoClientService: SSOServiceEndpoint[Future], accountEndpoint: AccountEndpoint[Future], producerTemplate: ProducerTemplate, towVsOneRepository: TowVsOneRepository) extends GameService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  // game center, ddz , key:memberId, value:game status
  private[this] val gameStatusPre = "gc.ddz.mi-gs:"
  private[this] val gameStatusExpireSeconds = 24 * 60 * 60
  // game center, ddz , key:memberId, value:akka address
  private[this] val addressPre = "gc.ddz.mi-addr:"
  private[this] val addressExpireSeconds = 24 * 60 * 60

  implicit def convert(s: towVsOneRepository.TmSeatRow): GameTurnResponse = {
    GameTurnResponse(s.gameId, s.gameType, s.cards, s.baseAmount, s.multiples, s.previousNickname, s.previousCardsCount, s.nextNickname, s.nextCardsCount, s.choosingLandlord, s.landlord, s.turnToPlay)
  }

  override def checkGameStatus(traceId: String, request: CheckGameStatusRequest): CheckGameStatusResponse = {
    val memberId: Long = request.memberId
    towVsOneRepository.findPlayingSeatByMemberAndStatus(memberId, Set(SeatStatus.Playing.getCode.toShort, SeatStatus.Dropped.getCode.toShort)) match {
      case Some(seat) =>
        val gamesStatus: GameStatus = towVsOneRepository.selectGameStatus(seat.gameId)
        gamesStatus match {
          case GameStatus.Playing =>
            request.fingerPrint.equals(seat.fingerPrint) match {
              case true =>
                CheckGameStatusResponse(code = "0", memberId = memberId, reconnect = true, turn = Some(seat))
              case false =>
                CheckGameStatusResponse(code = "0", memberId = memberId, reconnect = false)
            }
          case _ =>
            CheckGameStatusResponse(code = "0", memberId = memberId, reconnect = false)
        }
      case None =>
        CheckGameStatusResponse(code = "0", memberId = memberId, reconnect = false)
    }
  }

  override def joinGame(traceId: String, request: JoinGameRequest): GameBaseResponse = {
    val memberId: Long = request.memberId
    val deviceType: Int = request.deviceType
    val gameType: Int = request.gameType

    val accountResponse: DiamondAccountResponse = Await.result(accountEndpoint.getAccount(traceId, memberId, deviceType))

    towVsOneRepository.getRoomConfig(gameType, deviceType) match {
      case Some(config) =>
        val balance: Int = accountResponse.amount

        balance < config.minDiamondAmount match {
          case true => GameBaseResponse(code = ErrorCode.EC_GAME_NOT_ENOUGH_DIAMOND.getCode)
          case false => balance > config.maxDiamondAmount match {
            case true => GameBaseResponse(code = ErrorCode.EC_GAME_DIAMOND_TOO_MUCH.getCode)
            case false =>
              val game: JoinGameMessage = JoinGameMessage(traceId, request.socketId, request.ip, deviceType, request.fingerPrint, memberId, gameType)
              producerTemplate.send(config.topic, game.toByteArray)
              GameBaseResponse("0")
          }
        }

      case None =>
        GameBaseResponse(code = ErrorCode.EC_GC_CONFIG_ERROR.getCode)
    }
  }

  override def playCards(traceId: String, request: PlayCardsRequest): PlayCardsResponse = {
    null
  }


  override def playerOffline(traceId: String, socketUuid: Long, memberId: Long): GameBaseResponse = {
    towVsOneRepository.offline(socketUuid)
    towVsOneRepository.findPlayingSeatByMemberAndStatus(memberId, Set(SeatStatus.Playing.getCode.toShort)) match {
      case Some(seat) =>
        towVsOneRepository.playerDropped(seat.id, SeatStatus.Dropped)
      case None =>
    }
    GameBaseResponse("0")
  }

  override def playerOnline(traceId: String, r: OnlineRequest): GameBaseResponse = {
    towVsOneRepository.createOnlineRecord(towVsOneRepository.TmOnlineRecordRow(r.socketUuid, r.deviceType.toShort, r.fingerPrint, r.memberId, r.ip, r.host, new Timestamp(System.currentTimeMillis()), None, true))
    GameBaseResponse("0")
  }

  override def generateSocketId(traceId: String): GenerateSocketIdResponse = {
    GenerateSocketIdResponse(code = "0", socketId = towVsOneRepository.generateSocketId())
  }
}
