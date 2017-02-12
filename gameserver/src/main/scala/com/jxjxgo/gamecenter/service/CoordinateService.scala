package com.jxjxgo.gamecenter.service

import java.util
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.common.service.ConsumeService
import com.jxjxgo.gamecenter.domain.{PlayTable, Room, RoomConfig, Seat}
import com.jxjxgo.gamecenter.enumnate.{GameType, OnlineMemberGameStatus}
import com.jxjxgo.gamecenter.repo._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.StringBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/12/19.
  */
trait CoordinateService extends ConsumeService {

}

class CoordinateServiceImpl @Inject()(towVsOneRepository: TowVsOneRepository, redisClientTemplate: RedisClientTemplate, gameNotifyService: GameNotifyService) extends CoordinateService {
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
    Seat(r.id, r.traceId, r.status, r.memberId, r.cards, r.ip, r.fingerPrint, r.gmtCreate, r.gmtUpdate, r.gameId, r.gameType, r.multiples, r.baseAmount, r.previousNickname, r.previousCardsCount, r.nextNickname, r.nextCardsCount, r.choosingLandlord, r.landlord, r.turnToPlay)
  }

  def preparePlay(traceId: String, room: Room, table: PlayTable, gameType: Int) = {
    var shouldStartGame = true

    val waitingStatus: OnlineMemberGameStatus = OnlineMemberGameStatus.get(GameType.get(gameType), false)

    val seats: Seq[towVsOneRepository.TmSeatRow] = towVsOneRepository.getSeatsByRange(table.minSeatId, table.maxSeatId)
    var count: Int = seats.size
    seats.foreach {
      seat =>
        if (waitingStatus != getOnlineMemberGameStatus(seat.memberId)) {
          count = count - 1
          shouldStartGame = false
          towVsOneRepository.cleanSeat(room.id, table.id, seat.id, count)
        }
    }

    if (shouldStartGame && seats.size == 3) {
      startGame(traceId, room, table, gameType, seats)
    }
  }

  def startGame(traceId: String, room: Room, table: PlayTable, gameType: Int, seats: Seq[towVsOneRepository.TmSeatRow]) = {
    logger.info(s"game start, gameType:$gameType")
    logger.info(s"room:$room")
    logger.info(s"table:$table")
    logger.info(s"seats:$seats")
    val playingStatus: OnlineMemberGameStatus = OnlineMemberGameStatus.get(GameType.get(gameType), true)
    seats.foreach {
      seat => setOnlineMemberGameStatus(seat.memberId, playingStatus)
    }
    val seats1: Seat = seats(0)
    val seats2: Seat = seats(1)
    val seats3: Seat = seats(2)

    gameNotifyService.createGame(traceId, gameType, seats1, seats2, seats3)

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

  def findSeat(traceId: String, memberId: Long, gameType: Int, deviceType: Int): Unit = {
    logger.info(s"$traceId start find seat, memberId:$memberId, gameType:$gameType.")
    findRoom(gameType, deviceType) match {
      case Some(room) =>
        findTable(room) match {
          case Some(table) =>
            findSeatToDown(table) match {
              case Some(seat) =>
                sitDown(traceId, memberId, room, table, seat) match {
                  case true =>
                    table.currentPlayers match {
                      case 0 =>
                      case 1 =>
                      case 2 =>
                        preparePlay(traceId, room, table, gameType)
                      case _ =>
                        logger.info(s"system error, currentPlayers:${table.currentPlayers} .")
                        findSeat(traceId, memberId, gameType, deviceType)
                    }
                  case false => findSeat(traceId, memberId, gameType, deviceType)
                }
              case None => findSeat(traceId, memberId, gameType, deviceType)
            }
          case None => findSeat(traceId, memberId, gameType, deviceType)
        }
      case None => logger.info(s"gameType:$gameType did not find a room.")
        findSeat(traceId, memberId, gameType, deviceType)
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

  def sitDown(traceId: String, memberId: Long, room: Room, table: PlayTable, seat: Seat): Boolean = {
    towVsOneRepository.sitDown(traceId, memberId, room, table, seat)
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

  override def consume(data: Array[Byte]): Future[Unit] = {
    val promise: Promise[Unit] = Promise[Unit]()
    Future {
      //      findSeat()
    }
    promise.future
  }
}
