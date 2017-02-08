package com.jxjxgo.gamecenter.repo

import java.sql.{Connection, PreparedStatement}

import com.jxjxgo.gamecenter.enumnate.{RoomStatus, SeatStatus, TableStatus}
import com.jxjxgo.mysql.connection.DBComponent
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.JdbcBackend

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait CoordinateRepository extends Tables {
  this: DBComponent =>
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  import profile.api._

  def createGame(game: TmGameRow): Future[Int] = db.run {
    TmGame += game
  }

  def getRoomConfig(gameType: Int): Option[TmRoomConfigRow] = {
    val run: Future[Option[TmRoomConfigRow]] = db.run {
      TmRoomConfig.filter(_.gameType === gameType).result.headOption
    }
    Await.result(run, Duration.Inf)
  }

  def getWaitingRoom(minRoomId: Long, maxRoomId: Long): Option[TmRoomRow] = {
    Await.result(db.run {
      TmRoom.sortBy(_.id.asc).take(1).filter(r => (r.id between(minRoomId, maxRoomId)) && r.status === RoomStatus.Waiting.getCode.toByte).result.headOption
    }, Duration.Inf)
  }

  def getWaitingTable(minTableId: Long, maxTableId: Long): Option[TmTableRow] = {
    Await.result(db.run {
      TmTable.sortBy(_.id.asc).take(1).filter(t => (t.id between(minTableId, maxTableId)) && (t.status inSet (Set(TableStatus.Idle.getCode.toByte, TableStatus.Waiting.getCode.toByte)))).result.headOption
    }, Duration.Inf)
  }

  def getIdleSeat(minSeatId: Long, maxSeatId: Long): Option[TmSeatRow] = {
    Await.result(db.run {
      TmSeat.sortBy(_.id.asc).take(1).filter(r => (r.id between(minSeatId, maxSeatId)) && r.status === SeatStatus.Waiting.getCode.toByte).result.headOption
    }, Duration.Inf)
  }


  def sitDown(traceId: String, memberId: Long, room: TmRoomRow, table: TmTableRow, seat: TmSeatRow): Boolean = {
    var session: JdbcBackend#SessionDef = null
    var conn: Connection = null
    try {
      session = db.createSession()
      conn = session.conn
      conn.setAutoCommit(false)

      if (room.minTableId == table.id) {
        updateRoomStatus(conn, room.id, RoomStatus.Waiting)
      } else if (room.maxTableId == table.id && table.maxSeatId == seat.id) {
        updateRoomStatus(conn, room.id, RoomStatus.Full)
      }

      table.currentPlayers match {
        case 0 =>
          addTablePlayers(conn, table.id, TableStatus.Waiting, table.status, table.currentPlayers) match {
            case 1 =>
            case _ =>
              conn.rollback()
              return false
          }

        case 1 =>
          addTablePlayers(conn, table.id, TableStatus.Waiting, table.status, table.currentPlayers) match {
            case 1 =>
            case _ =>
              conn.rollback()
              return false
          }
        case 2 =>
          addTablePlayers(conn, table.id, TableStatus.Playing, table.status, table.currentPlayers) match {
            case 1 =>
            case _ =>
              conn.rollback()
              return false
          }
        case _ =>
          conn.rollback()
          return false
      }

      seatSitDown(conn, seat.id, memberId, SeatStatus.Playing, seat.status, traceId)
      conn.commit()
      true
    } catch {
      case ex: Exception => logger.error("db", ex)
        conn.rollback()
        false
    } finally {
      session.close()
    }
  }

  private def updateRoomStatus(conn: Connection, roomId: Long, newStatus: RoomStatus) = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_room SET status = ? WHERE id = ?")
    prepareStatement.setByte(1, newStatus.getCode.toByte)
    prepareStatement.setLong(2, roomId)
    prepareStatement.executeUpdate()
  }

  private def addTablePlayers(conn: Connection, tableId: Long, newStatus: TableStatus, status: Byte, currentCount: Long): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_table SET current_players = current_players + 1, status = ? WHERE id = ? AND status = ? AND current_players = ?")
    prepareStatement.setByte(1, newStatus.getCode.toByte)
    prepareStatement.setLong(2, tableId)
    prepareStatement.setByte(3, status)
    prepareStatement.setLong(4, currentCount)
    prepareStatement.executeUpdate()
  }

  private def plusTablePlayers(conn: Connection, tableId: Long, newStatus: TableStatus): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_table SET current_players = current_players - 1, status = ? WHERE id = ?")
    prepareStatement.setByte(1, newStatus.getCode.toByte)
    prepareStatement.setLong(2, tableId)
    prepareStatement.executeUpdate()
  }

  private def seatSitDown(conn: Connection, memberId: Long, seatId: Long, newStatus: SeatStatus, status: Byte, traceId: String): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_seat SET status = ?, member_id = ?, trace_id = ? WHERE id = ? AND status = ?")
    prepareStatement.setByte(1, newStatus.getCode.toByte)
    prepareStatement.setLong(2, memberId)
    prepareStatement.setString(3, traceId)
    prepareStatement.setLong(4, seatId)
    prepareStatement.setByte(5, status)
    prepareStatement.executeUpdate()
  }

  private def seatStandUp(conn: Connection, seatId: Long, newStatus: SeatStatus): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_seat SET status = ?, member_id = 0 WHERE id = ?")
    prepareStatement.setByte(1, newStatus.getCode.toByte)
    prepareStatement.setLong(2, seatId)
    prepareStatement.executeUpdate()
  }

  def getSeatsByRange(minSeatId: Long, maxSeatId: Long): Seq[TmSeatRow] = {
    val run: Future[Seq[TmSeatRow]] = db.run {
      TmSeat.sortBy(_.id.asc).filter(r => (r.id between(minSeatId, maxSeatId)) && (r.status === SeatStatus.Playing.getCode.toByte)).result
    }
    Await.result(run, Duration.Inf)
  }

  def cleanSeat(roomId: Long, tableId: Long, seatId: Long, count: Int) = {
    var session: JdbcBackend#SessionDef = null
    var conn: Connection = null
    try {
      session = db.createSession()
      conn = session.conn
      conn.setAutoCommit(false)

      val tableStatus: TableStatus = count match {
        case 3 => TableStatus.Waiting
        case 2 => TableStatus.Waiting
        case 1 => TableStatus.Idle
        case _ => TableStatus.Idle
      }

      seatStandUp(conn, seatId, SeatStatus.Waiting)
      plusTablePlayers(conn, tableId, tableStatus)
      updateRoomStatus(conn, roomId, RoomStatus.Waiting)

      conn.commit()
      true
    } catch {
      case ex: Exception => logger.error("db", ex)
        conn.rollback()
        false
    } finally {
      session.close()
    }
  }

  var gameIdIndex = -1

  def getNextGameId(): Long = {
    gameIdIndex = gameIdIndex + 1
    val sequenceName = "game_id_" + (gameIdIndex % 3)
    Await.result(db.run(sql"""select nextval($sequenceName)""".as[(Long)]), Duration.Inf).head
  }

  var playCardIdIndex = -1

  def getNextPlayCardId(): Long = {
    playCardIdIndex = playCardIdIndex + 1
    val sequenceName = "play_cards_id_" + (playCardIdIndex % 10)
    Await.result(db.run(sql"""select nextval($sequenceName)""".as[(Long)]), Duration.Inf).head
  }
}

class CoordinateRepositoryImpl extends CoordinateRepository with DBComponent