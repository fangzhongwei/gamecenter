package com.jxjxgo.gamecenter.repo

import java.sql.{Connection, PreparedStatement, Timestamp}

import com.jxjxgo.gamecenter.enumnate.{GameStatus, RoomStatus, SeatStatus, TableStatus}
import com.jxjxgo.mysql.connection.DBComponent
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.JdbcBackend

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait TowVsOneRepository extends Tables {
  this: DBComponent =>

  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

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
      TmRoom.sortBy(_.id.asc).take(1).filter(r => (r.id between(minRoomId, maxRoomId)) && r.status === RoomStatus.Waiting.getCode.toShort).result.headOption
    }, Duration.Inf)
  }

  def getWaitingTable(minTableId: Long, maxTableId: Long): Option[TmTableRow] = {
    Await.result(db.run {
      TmTable.sortBy(_.id.asc).take(1).filter(t => (t.id between(minTableId, maxTableId)) && (t.status inSet (Set(TableStatus.Idle.getCode.toShort, TableStatus.Waiting.getCode.toShort)))).result.headOption
    }, Duration.Inf)
  }

  def getIdleSeat(minSeatId: Long, maxSeatId: Long): Option[TmSeatRow] = {
    Await.result(db.run {
      TmSeat.sortBy(_.id.asc).take(1).filter(r => (r.id between(minSeatId, maxSeatId)) && r.status === SeatStatus.Idle.getCode.toShort).result.headOption
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
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setLong(2, roomId)
    prepareStatement.executeUpdate()
  }

  private def addTablePlayers(conn: Connection, tableId: Long, newStatus: TableStatus, status: Short, currentCount: Long): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_table SET current_players = current_players + 1, status = ? WHERE id = ? AND status = ? AND current_players = ?")
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setLong(2, tableId)
    prepareStatement.setShort(3, status)
    prepareStatement.setLong(4, currentCount)
    prepareStatement.executeUpdate()
  }

  private def plusTablePlayers(conn: Connection, tableId: Long, newStatus: TableStatus): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_table SET current_players = current_players - 1, status = ? WHERE id = ?")
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setLong(2, tableId)
    prepareStatement.executeUpdate()
  }

  private def seatSitDown(conn: Connection, memberId: Long, seatId: Long, newStatus: SeatStatus, status: Short, traceId: String): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_seat SET status = ?, member_id = ?, trace_id = ? WHERE id = ? AND status = ?")
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setLong(2, memberId)
    prepareStatement.setString(3, traceId)
    prepareStatement.setLong(4, seatId)
    prepareStatement.setShort(5, status)
    prepareStatement.executeUpdate()
  }

  private def seatStandUp(conn: Connection, seatId: Long, newStatus: SeatStatus): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_seat SET status = ?, member_id = 0 WHERE id = ?")
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setLong(2, seatId)
    prepareStatement.executeUpdate()
  }

  def getSeatsByRange(minSeatId: Long, maxSeatId: Long): Seq[TmSeatRow] = {
    val run: Future[Seq[TmSeatRow]] = db.run {
      TmSeat.sortBy(_.id.asc).filter { r => (r.id.between(minSeatId, maxSeatId) && r.status === SeatStatus.Playing.getCode.toShort) }.result
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

      seatStandUp(conn, seatId, SeatStatus.Idle)
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

  def findPlayingSeatByMemberAndStatus(memberId: Long, set: Set[Short]): Option[TmSeatRow] = {
    Await.result(db.run {
      TmSeat.sortBy(_.id.desc).filter { r => (r.memberId === memberId && (r.status inSet (set))) }.result.headOption
    }, Duration.Inf)
  }

  def updateOrInsertChannelAddress(row: TmChannelAddressRow) = {
    val memberId = row.memberId
    val result: Int = Await.result(db.run {
      TmChannelAddress.filter(_.memberId === memberId).length.result
    }, Duration.Inf)
    result > 0 match {
      case true => db.run(TmChannelAddress.filter(_.memberId === memberId).map(r => (r.host, r.gmtUpdate)).update(row.host, row.gmtUpdate))
      case false => db.run(TmChannelAddress += row)
    }
  }

  def createOnlineRecord(row: TmOnlineRecordRow) = db.run {
    TmOnlineRecord += row
  }

  def selectGameStatus(gameId: Long): GameStatus = {
    Await.result(db.run {
      TmGame.filter(_.id === gameId).map(_.status).result.head.map(GameStatus.get(_))
    }, Duration.Inf)
  }

  def playerDropped(seatId: Long, seatStatus: SeatStatus) = db.run {
    TmSeat.filter(_.id === seatId).map(r => (r.status, r.gmtUpdate)).update(seatStatus.getCode.toShort, new Timestamp(System.currentTimeMillis()))
  }

  def offline(socketUuid: String) = db.run {
    TmOnlineRecord.filter(_.socketUuid === socketUuid).map(r => (r.online, r.gmtOffline)).update(false, Some(new Timestamp(System.currentTimeMillis())))
  }

  def getNextGameId(): Long = {
    Await.result(db.run(sql"""select nextval('seq_game_id')""".as[(Long)]), Duration.Inf).head
  }

  def getNextPlayCardId(): Long = {
    Await.result(db.run(sql"""select nextval('seq_play_cards_id')""".as[(Long)]), Duration.Inf).head
  }
}

class TowVsOneRepositoryImpl extends TowVsOneRepository with DBComponent