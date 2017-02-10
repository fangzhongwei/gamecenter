package com.jxjxgo.gamecenter.service

import java.util
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.common.service.ConsumeService
import com.jxjxgo.gamecenter.repo._
import com.jxjxgo.gamecenter.enumnate.{GameType, OnlineMemberGameStatus}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.StringBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by fangzhongwei on 2016/12/19.
  */
trait CoordinateService extends ConsumeService {

}

class CoordinateServiceImpl @Inject()(coordinateRepository: TowVsOneRepository, redisClientTemplate: RedisClientTemplate, gameNotifyService: GameNotifyService) extends CoordinateService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private[this] val gameTypeRoomRangeMap: util.Map[Int, (Long, Long)] = new ConcurrentHashMap[Int, (Long, Long)]()
  // game center, ddz , key:memberId, value:game status
  private[this] val gameStatusPre = "gc.ddz.mi-gs:"
  private[this] val gameStatusExpireSeconds = 24 * 60 * 60

  def preparePlay(traceId: String, room: TmRoomRow, table: TmTableRow, gameType: Int) = {
    var shouldStartGame = true

    val waitingStatus: OnlineMemberGameStatus = OnlineMemberGameStatus.get(GameType.get(gameType), false)

    val seats: Seq[TmSeatRow] = coordinateRepository.getSeatsByRange(table.minSeatId, table.maxSeatId)
    var count: Int = seats.size
    seats.foreach {
      seat =>
        if (waitingStatus != getOnlineMemberGameStatus(seat.memberId)) {
          count = count - 1
          shouldStartGame = false
          coordinateRepository.cleanSeat(room.id, table.id, seat.id, count)
        }
    }

    if (shouldStartGame && seats.size == 3) {
      startGame(traceId, room, table, gameType, seats)
    }
  }


  def startGame(traceId: String, room: TmRoomRow, table: TmTableRow, gameType: Int, seats: Seq[TmSeatRow]) = {
    logger.info(s"game start, gameType:$gameType")
    logger.info(s"room:$room")
    logger.info(s"table:$table")
    logger.info(s"seats:$seats")
    val playingStatus: OnlineMemberGameStatus = OnlineMemberGameStatus.get(GameType.get(gameType), true)
    seats.foreach {
      seat => setOnlineMemberGameStatus(seat.memberId, playingStatus)
    }
    val seats1: TmSeatRow = seats(0)
    val seats2: TmSeatRow = seats(1)
    val seats3: TmSeatRow = seats(2)

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

  def findSeat(traceId: String, memberId: Long, gameType: Int): Unit = {
    logger.info(s"$traceId start find seat, memberId:$memberId, gameType:$gameType.")
    findRoom(gameType) match {
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
                        findSeat(traceId, memberId, gameType)
                    }
                  case false => findSeat(traceId, memberId, gameType)
                }
              case None => findSeat(traceId, memberId, gameType)
            }
          case None => findSeat(traceId, memberId, gameType)
        }
      case None => logger.info(s"gameType:$gameType did not find a room.")
        findSeat(traceId, memberId, gameType)
    }
  }

  def findRoom(gameType: Int): Option[TmRoomRow] = {
    var range: (Long, Long) = gameTypeRoomRangeMap.get(gameType)
    range == null match {
      case true =>
        val array: Array[String] = getRoomConfigByGameType(gameType).roomRanges.split("-")
        range = (array(0).toLong, array(1).toLong)
        gameTypeRoomRangeMap.put(gameType, range)
      case false =>
    }
    coordinateRepository.getWaitingRoom(range._1, range._2)
  }

  def findTable(room: TmRoomRow): Option[TmTableRow] = {
    coordinateRepository.getWaitingTable(room.minTableId, room.maxTableId)
  }

  def findSeatToDown(table: TmTableRow): Option[TmSeatRow] = {
    coordinateRepository.getIdleSeat(table.minSeatId, table.maxSeatId)
  }

  def sitDown(traceId:String, memberId: Long, room: TmRoomRow, table: TmTableRow, seat: TmSeatRow): Boolean = {
    coordinateRepository.sitDown(traceId, memberId, room, table, seat)
  }

  def getRoomConfigByGameType(gameType: Int): TmRoomConfigRow = {
    coordinateRepository.getRoomConfig(gameType) match {
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
