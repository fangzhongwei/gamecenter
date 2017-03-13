package com.jxjxgo.gamecenter.service

import java.sql.Timestamp
import java.util
import javax.inject.Inject

import com.jxjxgo.account.rpc.domain.{AccountEndpoint, DiamondAccountResponse, SettleRequest}
import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.kafka.template.ProducerTemplate
import com.jxjxgo.gamecenter.domain.Game
import com.jxjxgo.gamecenter.domain.mq.joingame.JoinGameMessage
import com.jxjxgo.gamecenter.enumnate.CardsType._
import com.jxjxgo.gamecenter.enumnate.{CardsType, GameStatus, PlayType, SeatStatus}
import com.jxjxgo.gamecenter.helper.{CardsJudgeHelper, TypeWithPoints}
import com.jxjxgo.gamecenter.repo.TowVsOneRepository
import com.jxjxgo.gamecenter.rpc.domain._
import com.jxjxgo.sso.rpc.domain.SSOServiceEndpoint
import com.twitter.util.{Await, Future}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/16.
  */
trait GameService {
  def takeLandlord(traceId: String, memberId: Long, seatId: Long, gameId: Long, take: Boolean): GameBaseResponse

  def playerOffline(traceId: String, socketUuid: Long, memberId: Long): GameBaseResponse

  def playerOnline(traceId: String, request: OnlineRequest): GameBaseResponse

  def generateSocketId(traceId: String): GenerateSocketIdResponse

  def checkGameStatus(traceId: String, request: CheckGameStatusRequest): CheckGameStatusResponse

  def joinGame(traceId: String, request: JoinGameRequest): GameBaseResponse

  def playCards(traceId: String, request: PlayCardsRequest): GameBaseResponse
}

class GameServiceImpl @Inject()(ssoClientService: SSOServiceEndpoint[Future], accountEndpoint: AccountEndpoint[Future], producerTemplate: ProducerTemplate, towVsOneRepository: TowVsOneRepository, coordinateService: CoordinateService) extends GameService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  // game center, ddz , key:memberId, value:game status
  private[this] val gameStatusPre = "gc.ddz.mi-gs:"
  private[this] val gameStatusExpireSeconds = 24 * 60 * 60
  // game center, ddz , key:memberId, value:akka address
  private[this] val addressPre = "gc.ddz.mi-addr:"
  private[this] val addressExpireSeconds = 24 * 60 * 60

  override def checkGameStatus(traceId: String, request: CheckGameStatusRequest): CheckGameStatusResponse = {
    val memberId: Long = request.memberId
    towVsOneRepository.findPlayingSeatByMemberAndStatus(memberId, Set(SeatStatus.Playing.getCode.toShort, SeatStatus.Dropped.getCode.toShort)) match {
      case Some(seat) =>
        val gamesStatus: GameStatus = towVsOneRepository.selectGameStatus(seat.gameId)
        gamesStatus == GameStatus.WaitingLandlord || gamesStatus == GameStatus.Playing match {
          case true =>
            request.fingerPrint.equals(seat.fingerPrint) match {
              case true =>
                CheckGameStatusResponse(code = "0", memberId = memberId, reconnect = true)
              case false =>
                CheckGameStatusResponse(code = "0", memberId = memberId, reconnect = false)
            }
          case false =>
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
              val game: JoinGameMessage = JoinGameMessage(traceId, request.socketId, request.ip, deviceType, request.fingerPrint, memberId, gameType, config.baseAmount)
              logger.info(s"send join message:$game")
              producerTemplate.send(config.topic, game.toByteArray)
              GameBaseResponse("0")
          }
        }

      case None =>
        logger.error(s"game config not found for type : $gameType")
        GameBaseResponse(code = ErrorCode.EC_GC_CONFIG_ERROR.getCode)
    }
  }

  override def playCards(traceId: String, request: PlayCardsRequest): GameBaseResponse = {
    val list: util.List[Integer] = new util.ArrayList[Integer]()
    request.playPoints.foreach(p => list.add(Integer.valueOf(p)))

    var typeWithPoints: TypeWithPoints = null
    request.cardsType.equals("Pass") match {
      case true => typeWithPoints = new TypeWithPoints(CardsType.Pass)
      case false => typeWithPoints = CardsJudgeHelper.GetInstance().judgeType(list)
    }

    typeWithPoints.getCardsType match {
      case Invalid =>
        logger.error(s"invalid cardType, request:$request")
        GameBaseResponse(ErrorCode.EC_GAME_INVALID_REQUEST_DATA.getCode)
      case _ =>
        val ct: CardsType = CardsType.valueOf(request.cardsType)

        if (ct == Invalid || ct == Exist) {
          throw ServiceException.make(ErrorCode.EC_GAME_INVALID_REQUEST_DATA);
        }

        ct == null match {
          case true =>
            GameBaseResponse(ErrorCode.EC_GAME_INVALID_REQUEST_DATA.getCode)
          case false =>
            val keyList: util.List[Integer] = convertList(request.keys)

            val twp: TypeWithPoints = parseTypeWithPoints(keyList, ct)

            twp.equals(typeWithPoints) match {
              case true =>
                val seqInGame: Short = request.seqInGame.toShort
                val gameId: Long = request.gameId
                towVsOneRepository.loadGame(gameId) match {
                  case Some(game) =>
                    val memberId = request.memberId
                    val seatId: Long = request.seatId
                    val seat: towVsOneRepository.TmSeatRow = towVsOneRepository.getSeat(seatId)
                    val playerCards = seat.cards
                    game.activePlayerId != request.memberId || game.status != GameStatus.Playing.getCode || game.seqInGame != seqInGame || !join(request.handPoints).equals(playerCards) || !listContains(request.handPoints, request.playPoints) match {
                      case true =>
                        logger.error(s"validate fail, request:$request.")
                        GameBaseResponse(ErrorCode.EC_GAME_INVALID_REQUEST_DATA.getCode)
                      case false =>
                        seat.memberId == memberId && seat.seqInGame == seqInGame && (game.seat1Id == seatId || game.seat2Id == seatId || game.seat3Id == seatId) match {
                          case true =>
                            (seqInGame > 1) match {
                              case true =>
                                val proRecord: towVsOneRepository.TmPlayRecordRow = towVsOneRepository.loadPlayRecord(gameId, (seqInGame - 1).toShort)
                                val proCardsType: CardsType = CardsType.valueOf(proRecord.cardsType)
                                var twp2Beat: TypeWithPoints = null
                                proRecord.cardsTypeForNext.equals("Exist") match {
                                  case true =>
                                    twp2Beat = new TypeWithPoints(CardsType.Exist)
                                  case false =>
                                    twp2Beat = parseTypeWithPoints(convertList(proRecord.keysForNext.split(",").map(_.toInt)), CardsType.valueOf(proRecord.cardsTypeForNext))
                                }

                                CardsJudgeHelper.GetInstance().canPlay(twp2Beat, typeWithPoints) match {
                                  case true =>
                                    recordPlay(traceId, request, game, twp2Beat, typeWithPoints, proCardsType, proRecord.playPoints)
                                  case false =>
                                    logger.error(s"PLAY_GAME_CARDS_NOT_BIG_ENOUGH :$request.")
                                    GameBaseResponse(ErrorCode.PLAY_GAME_CARDS_NOT_BIG_ENOUGH.getCode)
                                }
                              case false =>
                                typeWithPoints.getCardsType == Pass || typeWithPoints.getCardsType == Exist match {
                                  case true =>
                                    GameBaseResponse(ErrorCode.EC_GAME_INVALID_REQUEST_DATA.getCode)
                                  case false =>
                                    recordPlay(traceId, request, game, new TypeWithPoints(Exist), typeWithPoints, Exist, "")
                                }
                            }
                          case false =>
                            logger.error("params invalid.")
                            GameBaseResponse(ErrorCode.EC_GAME_INVALID_REQUEST_DATA.getCode)
                        }
                    }
                  case None =>
                    logger.error(s"game not exists, game id :${gameId}.")
                    GameBaseResponse(ErrorCode.EC_GAME_GAME_NOT_EXIST.getCode)
                }
              case false =>
                logger.error("params mismatch.")
                GameBaseResponse(ErrorCode.EC_GAME_INVALID_REQUEST_DATA.getCode)
            }
        }
    }
  }

  def recordPlay(traceId: String, r: PlayCardsRequest, game: Game, beatType: TypeWithPoints, twp: TypeWithPoints, proProCardsType: CardsType, proPlayPoints:String): GameBaseResponse = {
    var cardsTypeForNext: CardsType = null
    var keysForNext: String = null
    var showCardsForNext: String = null

    r.cardsType.equals("Pass") && proProCardsType == Pass match {
      case true =>
        cardsTypeForNext = Exist
        keysForNext = "-"
        showCardsForNext = "-"
      case false =>
        beatType.getCardsType match {
          case Exist =>
            cardsTypeForNext = twp.getCardsType
            keysForNext = twp.generateKeys();
            showCardsForNext = join(r.playPoints)
          case _ =>
            twp.getCardsType match {
              case Pass =>
                cardsTypeForNext = beatType.getCardsType
                keysForNext = beatType.generateKeys()
                showCardsForNext = proPlayPoints
              case _ =>
                cardsTypeForNext = twp.getCardsType
                keysForNext = twp.generateKeys()
                showCardsForNext = join(r.playPoints)
            }
        }
    }

    val playRecordRow: towVsOneRepository.TmPlayRecordRow = towVsOneRepository.TmPlayRecordRow(0, PlayType.Play.getCode, r.gameId, r.memberId, r.seqInGame.toShort, r.cardsType, join(r.keys), join(r.handPoints), join(r.playPoints), cardsTypeForNext.toString, keysForNext, new Timestamp(System.currentTimeMillis()))

    val seatId = r.seatId
    var proSeatId = 0L
    var nextSeatId = 0L
    var updateIndex = 1
    var nextActivePlayerId = 0L
    if (seatId == game.seat1Id) {
      updateIndex = 1
      proSeatId = game.seat3Id
      nextSeatId = game.seat2Id
      nextActivePlayerId = game.player2Id
    } else if (seatId == game.seat2Id) {
      updateIndex = 2
      proSeatId = game.seat1Id
      nextSeatId = game.seat3Id
      nextActivePlayerId = game.player3Id
    } else {
      updateIndex = 3
      proSeatId = game.seat2Id
      nextSeatId = game.seat1Id
      nextActivePlayerId = game.player1Id
    }

    val leftCount: Int = r.handPoints.size - r.playPoints.size
    val leftCards: String = joinLeft(r.handPoints, r.playPoints)

    var multiples: Int = game.multiples

    twp.getCardsType == Four || twp.getCardsType == DoubJoker match {
      case true => multiples *= 2
      case false =>
    }

    //    string cardsTypeCode2Beat;
    //    string cardsKeys2Beat;
    //    string cards4Show;
    //    string proPlayerAction;
//    val proCards: String = new StringBuilder("Exist:-:").append(landlordCards).append(":None").toString();

    val cardsInfo4Next = new StringBuilder(cardsTypeForNext.toString).append(':').append(keysForNext).append(':').append(showCardsForNext).append(':').append({
      r.cardsType match {
        case "Pass" => "Pass"
        case _ => "Play"
      }
    }).toString()

    towVsOneRepository.playCards(game, playRecordRow, updateIndex, seatId, proSeatId, nextSeatId, leftCount, leftCards, multiples, nextActivePlayerId, r.memberId, cardsInfo4Next)

    leftCount match {
      case 0 =>
        val gameId: Long = game.id
        val deviceType: Int = game.deviceType
        val memberId1: Long = game.player1Id
        var amount1: Int = 0
        val memberId2: Long = game.player2Id
        var amount2: Int = 0
        val memberId3: Long = game.player3Id
        var amount3: Int = 0

        val baseAmount: Int = game.baseAmount

        if (seatId == game.seat1Id) {
          amount1 = baseAmount * multiples * 2
          amount2 = -baseAmount * multiples
          amount3 = -baseAmount * multiples
        } else if (seatId == game.seat2Id) {
          amount1 = -baseAmount * multiples
          amount2 = baseAmount * multiples * 2
          amount3 = -baseAmount * multiples
        } else {
          amount1 = -baseAmount * multiples
          amount2 = -baseAmount * multiples
          amount3 = baseAmount * multiples * 2
        }
        accountEndpoint.settle(traceId, SettleRequest(gameId, deviceType, memberId1, amount1, memberId2, amount2, memberId3, amount3))
        towVsOneRepository.setGameStatus(gameId, GameStatus.Settled)
      case _ =>
    }

    coordinateService.notifySeat(game.seat1Id)
    coordinateService.notifySeat(game.seat2Id)
    coordinateService.notifySeat(game.seat3Id)

    GameBaseResponse("0")
  }

  def joinLeft(container: Seq[Int], list: Seq[Int]): String = {
    if (container.size == list.size) return ""
    val j: StringBuilder = new StringBuilder
    for (i <- container) {
      if (!list.contains(i)) j.append(i).append(',')
    }
    j.substring(0, j.length - 1)
  }

  def join(list: Seq[Int]): String = {
    if (list == null || list.size == 0) return "-"
    val j: StringBuilder = new StringBuilder
    for (i <- list) {
      j.append(i).append(',')
    }
    j.substring(0, j.length - 1)
  }

  private def listContains(container: Seq[Int], keys: Seq[Int]): Boolean = {
    var contains = true
    keys.foreach {
      k =>
        if (!container.contains(k)) {
          contains = false
        }
    }
    contains
  }

  private def convertList(seq: Seq[Int]): util.List[Integer] = {
    val keyList: util.List[Integer] = new util.ArrayList[Integer]()
    seq.foreach(p => keyList.add(Integer.valueOf(p)))
    keyList
  }

  def parseTypeWithPoints(keys: util.List[Integer], ct: CardsType): TypeWithPoints = {
    val twp: TypeWithPoints = new TypeWithPoints()
    twp.setCardsType(ct)
    if (ct != Pass) {
      ct match {
        case Single => twp.setP(keys.get(0))
        case Doub => twp.setP(keys.get(0))
        case Four => twp.setP(keys.get(0))
        case _ =>
          twp.setPs(keys)
      }
    }
    twp
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

  override def takeLandlord(traceId: String, memberId: Long, seatId: Long, gameId: Long, take: Boolean): GameBaseResponse = {
    towVsOneRepository.loadGame(gameId) match {
      case Some(game) =>
        var proMemberId = 0L
        var nextMemberId = 0L

        var sid = 0L
        var proSeatId = 0L
        var nextSeatId = 0L

        if (memberId == game.player1Id) {
          proMemberId = game.player3Id
          nextMemberId = game.player2Id
          sid = game.seat1Id
          proSeatId = game.seat3Id
          nextSeatId = game.seat2Id
        } else if (memberId == game.player2Id) {
          proMemberId = game.player1Id
          nextMemberId = game.player3Id
          sid = game.seat2Id
          proSeatId = game.seat1Id
          nextSeatId = game.seat3Id
        } else {
          proMemberId = game.player2Id
          nextMemberId = game.player1Id
          sid = game.seat3Id
          proSeatId = game.seat2Id
          nextSeatId = game.seat1Id
        }

        sid == seatId match {
          case true =>
            game.activePlayerId == memberId && game.status == GameStatus.WaitingLandlord.getCode && game.seqInGame == 0 match {
              case true =>
                val seat: towVsOneRepository.TmSeatRow = towVsOneRepository.getSeat(seatId)
                seat.memberId == memberId && seat.seqInGame == 0 && (game.seat1Id == seatId || game.seat2Id == seatId || game.seat3Id == seatId) match {
                  case true =>
                    take match {
                      case true =>
                        val newProCards: String = "Exist:-:-:None"
                        val seat: towVsOneRepository.TmSeatRow = towVsOneRepository.getSeat(seatId)
                        val newCardList: util.List[Integer] = new util.ArrayList[Integer]()
                        seat.cards.split(",").foreach(p => newCardList.add(Integer.valueOf(p)))
                        seat.landlordCards.split(",").foreach(p => newCardList.add(Integer.valueOf(p)))
                        CardsJudgeHelper.GetInstance().sort(newCardList)
                        val newCards: String = CardsJudgeHelper.GetInstance().join(newCardList)
                        towVsOneRepository.takeLandlord(gameId, memberId, proMemberId, nextMemberId, seatId, proSeatId, nextSeatId, newCards, newProCards)
                        coordinateService.notifySeat(game.seat1Id)
                        coordinateService.notifySeat(game.seat2Id)
                        coordinateService.notifySeat(game.seat3Id)
                        GameBaseResponse("0")
                      case false =>
                        val passCount: Int = towVsOneRepository.gamePassCount(gameId)
                        passCount >= 2 match {
                          case true =>
                            towVsOneRepository.passLandlord3th(gameId, memberId, proMemberId, nextMemberId, seatId, proSeatId, nextSeatId)
                            coordinateService.createGame(traceId, game.seat1Id, game.seat2Id, game.seat3Id, game.player1Id, game.player2Id, game.player3Id, game.gameType, game.deviceType, game.baseAmount)
                          case false =>
                            towVsOneRepository.passLandlord(gameId, memberId, proMemberId, nextMemberId, seatId, proSeatId, nextSeatId)
                        }
                        coordinateService.notifySeat(game.seat1Id)
                        coordinateService.notifySeat(game.seat2Id)
                        coordinateService.notifySeat(game.seat3Id)
                        GameBaseResponse("0")
                    }
                  case false =>
                    throw ServiceException.make(ErrorCode.EC_GAME_INVALID_REQUEST_DATA)
                }
              case false =>
                throw ServiceException.make(ErrorCode.EC_GAME_INVALID_REQUEST_DATA)
            }
          case false =>
            throw ServiceException.make(ErrorCode.EC_GAME_INVALID_REQUEST_DATA)
        }
      case None =>
        logger.error(s"game not found, game id:$gameId")
        throw ServiceException.make(ErrorCode.EC_GAME_GAME_NOT_EXIST)
    }
  }
}
