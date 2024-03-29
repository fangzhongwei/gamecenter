package com.jxjxgo.gamecenter.service

import java.sql.Timestamp
import java.util
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.mq.service.ConsumerService
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.gamecenter.domain.mq.joingame.JoinGameMessage
import com.jxjxgo.gamecenter.domain.{PlayTable, Room, RoomConfig, Seat}
import com.jxjxgo.gamecenter.enumnate.{GameStatus, PlayStatus, SeatStatus}
import com.jxjxgo.gamecenter.helper.CardsHelper
import com.jxjxgo.gamecenter.repo._
import com.jxjxgo.memberber.rpc.domain.{MemberEndpoint, MemberResponse}
import com.twitter.util.{Await, Future}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

/**
  * Created by fangzhongwei on 2016/12/19.
  */
trait CoordinateService extends ConsumerService {
  def notifySeat(seatId: Long): scala.concurrent.Future[Unit]

  def createGame(traceId: String, seatId1: Long, seatId2: Long, seatId3: Long, memberId1: Long, memberId2: Long, memberId3: Long, gameType: Int, deviceType: Int, baseAmount: Int): Unit
}

class CoordinateServiceImpl @Inject()(towVsOneRepository: TowVsOneRepository, redisClientTemplate: RedisClientTemplate, gameNotifyService: GameNotifyService, memberClientService: MemberEndpoint[Future]) extends CoordinateService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private[this] val gameTypeRoomRangeMap: util.Map[Int, (Long, Long)] = new ConcurrentHashMap[Int, (Long, Long)]()
  // game center, ddz , key:memberId, value:game status
  private[this] val gameStatusPre = "gc.ddz.mi-gs:"
  private[this] val gameStatusExpireSeconds = 24 * 60 * 60

  implicit def convertRoomConfig(r: towVsOneRepository.TmRoomConfigRow): RoomConfig = {
    RoomConfig(r.gameType, r.deviceType: Int, r.minDiamondAmount, r.maxDiamondAmount, r.roomRanges, r.gmtCreate, r.gmtUpdate, r.topic)
  }

  implicit def convertRoom(r: towVsOneRepository.TmRoomRow): Room = {
    Room(r.id, r.name, r.status, r.minTableId, r.maxTableId, r.seatCapacity, r.currentPlayers, r.gmtCreate, r.gmtUpdate)
  }

  implicit def convertTable(t: towVsOneRepository.TmTableRow): PlayTable = {
    PlayTable(t.id, t.name, t.status, t.seatCapacity, t.minSeatId, t.maxSeatId, t.currentPlayers, t.gmtCreate, t.gmtUpdate)
  }

  implicit def convertSeat(r: towVsOneRepository.TmSeatRow): Seat = {
    Seat(r.id, r.status, r.memberId, r.cards, r.landlordCards, r.proCardsInfo, r.fingerPrint, r.gameId, r.gameType, r.deviceType, r.multiples, r.baseAmount, r.landlordPosition, r.previousNickname, r.previousCardsCount, r.nextNickname, r.nextCardsCount, PlayStatus.get(r.playStatus), r.seqInGame, r.landlord, r.socketId, r.gmtUpdate)
  }

  def preparePlay(traceId: String, room: Room, table: PlayTable, m: JoinGameMessage) = {
    var shouldStartGame = true

    val seats: Seq[towVsOneRepository.TmSeatRow] = towVsOneRepository.getWaitingSeatsByRange(table.minSeatId, table.maxSeatId)
    var count: Int = seats.size
    seats.foreach {
      seat =>
        if (SeatStatus.WaitingStart != towVsOneRepository.getSeatStatus(seat.id)) {
          count = count - 1
          shouldStartGame = false
          towVsOneRepository.cleanSeat(room.id, table.id, seat.id, count)
        }
    }

    if (shouldStartGame && count == 3) {
      startGame(traceId, m, room, table, seats)
    }
  }

  implicit def list2String(list: List[Int]): String = list.mkString(",")

  def startGame(traceId: String, m: JoinGameMessage, room: Room, table: PlayTable, seats: Seq[towVsOneRepository.TmSeatRow]) = {
    createGame(traceId, seats(0).id, seats(1).id, seats(2).id, seats(0).memberId, seats(1).memberId, seats(2).memberId, m.gameType, m.deviceType, m.baseAmount)
  }

  def createGame(traceId: String, seatId1: Long, seatId2: Long, seatId3: Long, memberId1: Long, memberId2: Long, memberId3: Long, gameType: Int, deviceType: Int, baseAmount: Int): Unit = {

    val (player1CardsList, player2CardsList, player3CardsList, dzCardsList) = CardsHelper.initCards()
    val gameId: Long = towVsOneRepository.getNextGameId()
    val now = new Timestamp(System.currentTimeMillis())

    val memberResponse1: MemberResponse = Await.result(memberClientService.getMemberById(traceId, memberId1))
    val memberResponse2: MemberResponse = Await.result(memberClientService.getMemberById(traceId, memberId2))
    val memberResponse3: MemberResponse = Await.result(memberClientService.getMemberById(traceId, memberId3))

    val game: towVsOneRepository.TmGameRow = towVsOneRepository.TmGameRow(gameId, gameType, deviceType, baseAmount, 1, GameStatus.WaitingLandlord.getCode, memberId1, memberId2, memberId3, seatId1, seatId2, seatId3, 0, memberId1, player1CardsList, player2CardsList, player3CardsList, dzCardsList, 0, 0, now, now)
    towVsOneRepository.createGame(game, seatId1, seatId2, seatId3, player1CardsList, player2CardsList, player3CardsList, dzCardsList, memberResponse1.nickName, memberResponse2.nickName, memberResponse3.nickName)

    notifySeat(seatId1)
    notifySeat(seatId2)
    notifySeat(seatId3)
  }

  def notifySeat(seatId: Long): scala.concurrent.Future[Unit] = {
    val promise: Promise[Unit] = Promise[Unit]()
    scala.concurrent.Future {
      val seat: towVsOneRepository.TmSeatRow = towVsOneRepository.getSeat(seatId)
      gameNotifyService.notifySeatInfo(seat)
      promise.success()
    }
    promise.future
  }

  def findSeat(traceId: String, m: JoinGameMessage): Unit = {
    val memberId: Long = m.memberId
    val gameType: Int = m.gameType
    val deviceType: Int = m.deviceType

    logger.info(s"$traceId start find seat, memberId:$memberId, gameType:$gameType.")
    findRoom(gameType, deviceType) match {
      case Some(room) =>
        findTable(room) match {
          case Some(table) =>
            findSeatToDown(table) match {
              case Some(seat) =>
                sitDown(traceId, m, room, table, seat) match {
                  case true =>
                    table.currentPlayers match {
                      case 0 =>
                      case 1 =>
                      case 2 =>
                        preparePlay(traceId, room, table, m)
                      case _ =>
                        logger.info(s"system error, currentPlayers:${table.currentPlayers} .")
                        findSeat(traceId, m)
                    }
                  case false => findSeat(traceId, m)
                }
              case None => findSeat(traceId, m)
            }
          case None => findSeat(traceId, m)
        }
      case None => logger.info(s"gameType:$gameType did not find a room.")
        findSeat(traceId, m)
    }
  }

  def findRoom(gameType: Int, deviceType: Int): Option[Room] = {
    var range: (Long, Long) = gameTypeRoomRangeMap.get(gameType)
    range == null match {
      case true =>
        val array: Array[String] = getRoomConfigByGameType(gameType, deviceType).roomRanges.split("-")
        range = (array(0).toLong, array(1).toLong)
        gameTypeRoomRangeMap.put(gameType, range)
      case false =>
    }
    towVsOneRepository.getWaitingRoom(range._1, range._2) match {
      case Some(r) => Some(r)
      case None => None
    }
  }

  def findTable(room: Room): Option[PlayTable] = {
    towVsOneRepository.getWaitingTable(room.minTableId, room.maxTableId) match {
      case Some(t) => Some(t)
      case None => None
    }
  }

  def findSeatToDown(table: PlayTable): Option[Seat] = {
    towVsOneRepository.getIdleSeat(table.minSeatId, table.maxSeatId) match {
      case Some(s) => Some(s)
      case None => None
    }
  }

  def sitDown(traceId: String, m: JoinGameMessage, room: Room, table: PlayTable, seat: Seat): Boolean = {
    towVsOneRepository.sitDown(traceId, m, room, table, seat)
  }

  def getRoomConfigByGameType(gameType: Int, deviceType: Int): RoomConfig = {
    towVsOneRepository.getRoomConfig(gameType, deviceType) match {
      case Some(conf) =>
        conf
      case None =>
        logger.error(s"gameType:$gameType not config in table tm_room_config.")
        throw ServiceException.make(ErrorCode.EC_GC_CONFIG_ERROR)
    }
  }

  def parseRooms(roomRanges: String): java.util.List[Int] = {
    val list: java.util.List[Int] = new util.ArrayList[Int]()
    roomRanges.split(",").foreach {
      item =>
        val strings: Array[String] = item.split("-")
        strings.length match {
          case 1 => list.add(strings(0).toInt)
          case 2 => for (i <- strings(0).toInt to strings(1).toInt) list.add(i.toInt)
          case _ =>
        }
    }
    list
  }

  override def consume(list: ListBuffer[Array[Byte]]): Unit = {
    val iterator: Iterator[Array[Byte]] = list.iterator
    logger.info(s"receive ${list.size} join message.")
    while (iterator.hasNext) {
      try {
        val array: Array[Byte] = iterator.next()
        val m: JoinGameMessage = JoinGameMessage.parseFrom(array)
        logger.info(s"receive join message:$m")
        findSeat(m.traceId, m)
      } catch {
        case ex: Exception => {
          logger.error("consumer error", ex)
        }
      }
    }
  }
}
