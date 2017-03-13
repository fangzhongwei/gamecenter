package com.jxjxgo.gamecenter.repo

import java.sql.{Connection, PreparedStatement, Timestamp}

import com.jxjxgo.gamecenter.domain.mq.joingame.JoinGameMessage
import com.jxjxgo.gamecenter.domain.{Game, PlayTable, Room, Seat}
import com.jxjxgo.gamecenter.enumnate._
import com.jxjxgo.mysql.connection.DBComponent
import org.slf4j.{Logger, LoggerFactory}
import slick.jdbc.JdbcBackend

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait TowVsOneRepository extends Tables {
  this: DBComponent =>

  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  import profile.api._

  implicit def convertGame(r: TmGameRow): Game = {
    Game(r.id, r.gameType, r.deviceType, r.baseAmount, r.multiples, r.status, r.player1Id, r.player2Id, r.player3Id, r.seat1Id, r.seat2Id, r.seat3Id, r.seqInGame, r.activePlayerId, r.player1Cards, r.player2Cards, r.player3Cards, r.outsideCards, r.landlordId, r.winnerId, r.gmtCreate, r.gmtUpdate)
  }

  def createGame(game: TmGameRow, seatId1: Long, seatId2: Long, seatId3: Long, player1CardsList: List[Int], player2CardsList: List[Int], player3CardsList: List[Int], dzCardsList: List[Int], nickName1: String, nickName2: String, nickName3: String): Unit = {
    val cards1: String = player1CardsList.mkString(",")
    val cards2: String = player2CardsList.mkString(",")
    val cards3: String = player3CardsList.mkString(",")
    val landlordCards: String = dzCardsList.mkString(",")
    val now = new Timestamp(System.currentTimeMillis())

    //    string cardsTypeCode2Beat;
    //    string cardsKeys2Beat;
    //    string cards4Show;
    //    string proPlayerAction;

    val proCards: String = new StringBuilder("Exist:-:").append(landlordCards).append(":None").toString();

    val tran = (for {
      ns <- TmGame += game
      _ <- TmSeat.filter(_.id === seatId1).map(s => (s.status, s.gameId, s.multiples, s.cards, s.landlordCards, s.proCardsInfo, s.previousCardsCount, s.previousNickname, s.nextCardsCount, s.nextNickname, s.playStatus, s.landlord, s.gmtUpdate)).update(SeatStatus.Playing.getCode.toShort, game.id, 1, cards1, landlordCards, proCards, player3CardsList.size.toShort, nickName3, player2CardsList.size.toShort, nickName2, PlayStatus.DecideToBeLandlord.getCode, false, now)
      _ <- TmSeat.filter(_.id === seatId2).map(s => (s.status, s.gameId, s.multiples, s.cards, s.landlordCards, s.proCardsInfo, s.previousCardsCount, s.previousNickname, s.nextCardsCount, s.nextNickname, s.playStatus, s.landlord, s.gmtUpdate)).update(SeatStatus.Playing.getCode.toShort, game.id, 1, cards2, landlordCards, proCards, player1CardsList.size.toShort, nickName1, player3CardsList.size.toShort, nickName3, PlayStatus.WaitingOtherPlay.getCode, false, now)
      _ <- TmSeat.filter(_.id === seatId3).map(s => (s.status, s.gameId, s.multiples, s.cards, s.landlordCards, s.proCardsInfo, s.previousCardsCount, s.previousNickname, s.nextCardsCount, s.nextNickname, s.playStatus, s.landlord, s.gmtUpdate)).update(SeatStatus.Playing.getCode.toShort, game.id, 1, cards3, landlordCards, proCards, player2CardsList.size.toShort, nickName2, player1CardsList.size.toShort, nickName1, PlayStatus.WaitingOtherPlay.getCode, false, now)
    } yield ()).transactionally
    Await.result(db.run(tran), Duration.Inf)
  }

  def getRoomConfig(gameType: Int, deviceType: Int): Option[TmRoomConfigRow] = {
    val run: Future[Option[TmRoomConfigRow]] = db.run {
      TmRoomConfig.filter(r => (r.gameType === gameType && r.deviceType === deviceType)).result.headOption
    }
    Await.result(run, Duration.Inf)
  }

  def getWaitingRoom(minRoomId: Long, maxRoomId: Long): Option[TmRoomRow] = {
    Await.result(db.run {
      TmRoom.sortBy(_.id.asc).filter(r => (r.id between(minRoomId, maxRoomId)) && r.status === RoomStatus.Waiting.getCode.toShort).take(1).result.headOption
    }, Duration.Inf)
  }

  def getWaitingTable(minTableId: Long, maxTableId: Long): Option[TmTableRow] = {
    Await.result(db.run {
      TmTable.sortBy(_.id.asc).filter(t => (t.id between(minTableId, maxTableId)) && (t.status inSet (Set(TableStatus.Idle.getCode.toShort, TableStatus.Waiting.getCode.toShort)))).take(1).result.headOption
    }, Duration.Inf)
  }

  def getIdleSeat(minSeatId: Long, maxSeatId: Long): Option[TmSeatRow] = {
    Await.result(db.run {
      TmSeat.sortBy(_.id.asc).filter(r => (r.id between(minSeatId, maxSeatId)) && r.status === SeatStatus.Idle.getCode.toShort).take(1).result.headOption
    }, Duration.Inf)
  }

  def sitDown(traceId: String, m: JoinGameMessage, room: Room, table: PlayTable, seat: Seat): Boolean = {
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
      seatSitDown(conn, seat.id, m, SeatStatus.WaitingStart, seat.status, m.fingerPrint)
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

  private def seatSitDown(conn: Connection, seatId: Long, m: JoinGameMessage, newStatus: SeatStatus, status: Short, fingerPrint: String): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_seat SET status = ?, device_type = ?, game_type = ?, base_amount = ?, member_id = ?, finger_print = ?, socket_id = ? WHERE id = ? AND status = ?")
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setInt(2, m.deviceType)
    prepareStatement.setInt(3, m.gameType)
    prepareStatement.setInt(4, m.baseAmount)
    prepareStatement.setLong(5, m.memberId)
    prepareStatement.setString(6, fingerPrint)
    prepareStatement.setLong(7, m.socketId)

    prepareStatement.setLong(8, seatId)
    prepareStatement.setShort(9, status)
    prepareStatement.executeUpdate()
  }

  private def seatStandUp(conn: Connection, seatId: Long, newStatus: SeatStatus): Int = {
    val prepareStatement: PreparedStatement = conn.prepareStatement("UPDATE tm_seat SET status = ?, member_id = 0 WHERE id = ?")
    prepareStatement.setShort(1, newStatus.getCode.toShort)
    prepareStatement.setLong(2, seatId)
    prepareStatement.executeUpdate()
  }

  def getWaitingSeatsByRange(minSeatId: Long, maxSeatId: Long): Seq[TmSeatRow] = {
    val run: Future[Seq[TmSeatRow]] = db.run {
      TmSeat.sortBy(_.id.asc).filter { r => (r.id.between(minSeatId, maxSeatId) && r.status === SeatStatus.WaitingStart.getCode.toShort) }.result
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

  def createOnlineRecord(row: TmOnlineRecordRow) = db.run {
    TmOnlineRecord += row
  }

  def selectGameStatus(gameId: Long): GameStatus = {
    Await.result(db.run {
      TmGame.filter(_.id === gameId).map(_.status).result.head.map(GameStatus.get(_))
    }, Duration.Inf)
  }

  def getSeat(seatId: Long): TmSeatRow = {
    Await.result(db.run {
      TmSeat.filter(_.id === seatId).result.head
    }, Duration.Inf)
  }

  def getOnlineRecord(socketId: Long): TmOnlineRecordRow = {
    Await.result(db.run {
      TmOnlineRecord.filter(_.socketId === socketId).result.head
    }, Duration.Inf)
  }

  def getSeatStatus(seatId: Long): SeatStatus = {
    Await.result(db.run {
      TmSeat.filter(_.id === seatId).map(_.status).result.head.map(SeatStatus.get(_))
    }, Duration.Inf)
  }

  def playerDropped(seatId: Long, seatStatus: SeatStatus) = db.run {
    TmSeat.filter(_.id === seatId).map(r => (r.status, r.gmtUpdate)).update(seatStatus.getCode.toShort, new Timestamp(System.currentTimeMillis()))
  }

  def offline(socketUuid: Long) = db.run {
    TmOnlineRecord.filter(_.socketId === socketUuid).map(r => (r.online, r.gmtOffline)).update(false, Some(new Timestamp(System.currentTimeMillis())))
  }

  def getNextGameId(): Long = {
    Await.result(db.run(sql"""select nextval('seq_game_id')""".as[(Long)]), Duration.Inf).head
  }

  def getNextPlayCardId(): Long = {
    Await.result(db.run(sql"""select nextval('seq_play_cards_id')""".as[(Long)]), Duration.Inf).head
  }

  def generateSocketId(): Long = {
    Await.result(db.run(sql"""select nextval('seq_socket_id')""".as[(Long)]), Duration.Inf).head
  }

  def loadGame(gameId: Long): Option[Game] = {
    Await.result(db.run(TmGame.filter(_.id === gameId).result.headOption), Duration.Inf) match {
      case Some(r) => Some(r)
      case None => None
    }
  }

  def takeLandlord(gameId: Long, memberId: Long, proMemberId: Long, nextMemberId: Long, seatId: Long, proSeatId: Long, nextSeatId: Long, newCards: String, newProCards: String): Unit = {
    val now: Timestamp = new Timestamp(System.currentTimeMillis())
    val a = (for {
      ns <- TmGame.filter(_.id === gameId).map(g => (g.status, g.landlordId, g.activePlayerId, g.seqInGame, g.gmtUpdate)).update(GameStatus.Playing.getCode, memberId, memberId, 1, now)
      _ <- TmSeat.filter(t => t.id === seatId).map(s => (s.cards, s.proCardsInfo, s.playStatus, s.seqInGame, s.landlord, s.landlordPosition, s.gmtUpdate)).update(newCards, newProCards, PlayStatus.TurnToPlay.getCode, 1, true, 'S', now)
      _ <- TmSeat.filter(t => t.id === proSeatId).map(s => (s.proCardsInfo, s.playStatus, s.seqInGame, s.landlord, s.landlordPosition, s.gmtUpdate)).update(newProCards, PlayStatus.WaitingOtherPlay.getCode, 0, false, 'N', now)
      _ <- TmSeat.filter(t => t.id === nextSeatId).map(s => (s.proCardsInfo, s.playStatus, s.seqInGame, s.landlord, s.landlordPosition, s.gmtUpdate)).update(newProCards, PlayStatus.WaitingOtherPlay.getCode, 0, false, 'P', now)
      _ <- TmPlayRecord += TmPlayRecordRow(0, PlayType.DecideYes.getCode, gameId, memberId, 0, "", "", "", "", "", "", now)
    } yield ()).transactionally
    Await.result(db.run(a), Duration.Inf)
  }

  def passLandlord(gameId: Long, memberId: Long, proMemberId: Long, nextMemberId: Long, seatId: Long, proSeatId: Long, nextSeatId: Long): Unit = {
    val now: Timestamp = new Timestamp(System.currentTimeMillis())
    val a = (for {
      ns <- TmGame.filter(_.id === gameId).map(g => (g.status, g.landlordId, g.activePlayerId, g.seqInGame, g.gmtUpdate)).update(GameStatus.WaitingLandlord.getCode, 0, nextMemberId, 0, now)
      _ <- TmSeat.filter(t => t.id === seatId).map(s => (s.playStatus, s.seqInGame, s.landlord, s.gmtUpdate)).update(PlayStatus.WaitingOtherPlay.getCode, 0, false, now)
      _ <- TmSeat.filter(t => t.id === proSeatId).map(s => (s.playStatus, s.seqInGame, s.landlord, s.gmtUpdate)).update(PlayStatus.WaitingOtherPlay.getCode, 0, false, now)
      _ <- TmSeat.filter(t => t.id === nextSeatId).map(s => (s.playStatus, s.seqInGame, s.landlord, s.gmtUpdate)).update(PlayStatus.DecideToBeLandlord.getCode, 0, false, now)
      _ <- TmPlayRecord += TmPlayRecordRow(0, PlayType.DecideNo.getCode, gameId, memberId, 0, "", "", "", "", "", "", now)
    } yield ()).transactionally
    Await.result(db.run(a), Duration.Inf)
  }

  def passLandlord3th(gameId: Long, memberId: Long, proMemberId: Long, nextMemberId: Long, seatId: Long, proSeatId: Long, nextSeatId: Long): Unit = {
    val now: Timestamp = new Timestamp(System.currentTimeMillis())
    val a = (for {
      ns <- TmGame.filter(_.id === gameId).map(g => (g.status, g.activePlayerId, g.winnerId, g.gmtUpdate)).update(GameStatus.Aborted.getCode, 0, 0, now)
      _ <- TmSeat.filter(t => t.id === seatId).map(s => (s.playStatus, s.seqInGame, s.landlord, s.gmtUpdate)).update(PlayStatus.WaitingStart.getCode, 0, false, now)
      _ <- TmSeat.filter(t => t.id === proSeatId).map(s => (s.playStatus, s.seqInGame, s.landlord, s.gmtUpdate)).update(PlayStatus.WaitingStart.getCode, 0, false, now)
      _ <- TmSeat.filter(t => t.id === nextSeatId).map(s => (s.playStatus, s.seqInGame, s.landlord, s.gmtUpdate)).update(PlayStatus.WaitingStart.getCode, 0, false, now)
      _ <- TmPlayRecord += TmPlayRecordRow(0, PlayType.DecideNo.getCode, gameId, memberId, 0, "", "", "", "", "", "", now)
    } yield ()).transactionally
    Await.result(db.run(a), Duration.Inf)
  }

  //  def getSeatIdByGameAndMember(gameId: Long, memberId: Long): Long = {
  //    Await.result(db.run(sql"""SELECT id FROM tm_seat WHERE game_id = $gameId AND member_id = $memberId """.as[(Long)]), Duration.Inf).head
  //  }

  def gamePassCount(gameId: Long): Int = {
    val code: Short = PlayType.DecideNo.getCode
    Await.result(db.run(sql"""SELECT COUNT(*) FROM tm_play_record WHERE game_id = $gameId AND play_type = $code """.as[(Int)]), Duration.Inf).head
  }

  def loadPlayRecord(gameId: Long, seqInGame: Short): TmPlayRecordRow = {
    Await.result(db.run(TmPlayRecord.filter {
      r => (r.gameId === gameId && r.seqInGame === seqInGame)
    }.result.head), Duration.Inf)
  }

  def playCards(game: Game, playRecordRow: TmPlayRecordRow, updateIndex: Int, seatId: Long, proSeatId: Long, nextSeatId: Long, leftCount: Int, leftCards: String, multiples: Int, nextActivePlayerId: Long, playerId: Long, cardsInfo4Next: String) = {

    //    proCardsInfo:
    //    string cardsTypeCode2Beat;
    //    string cardsKeys2Beat;
    //    string cards4Show;
    //    string proPlayerAction;

    val now = new Timestamp(System.currentTimeMillis())

    val activeSeqInGame: Int = game.seqInGame + 1
    val tran = (for {
      ns <- TmGame.filter(_.id === game.id).map(g => ( {
        updateIndex match {
          case 1 => g.player1Cards
          case 2 => g.player2Cards
          case _ => g.player3Cards
        }
      }, g.activePlayerId, g.seqInGame, g.multiples, g.winnerId, g.status, g.gmtUpdate))
        .update(leftCards, nextActivePlayerId, activeSeqInGame, multiples,
          leftCount match {
            case 0 => playerId
            case _ => 0L
          },
          leftCount match {
            case 0 => GameStatus.Finished.getCode
            case _ => GameStatus.Playing.getCode
          },
          now)
      _ <- TmSeat.filter(_.id === seatId).map(s => (s.cards, s.multiples, s.playStatus, s.seqInGame, s.gmtUpdate)).update(leftCards, multiples, leftCount match {
        case 0 => PlayStatus.End.getCode
        case _ => PlayStatus.WaitingOtherPlay.getCode
      }, 0, now)
      _ <- TmSeat.filter(_.id === proSeatId).map(s => (s.nextCardsCount, s.multiples, s.proCardsInfo, s.playStatus, s.gmtUpdate)).update(leftCount.toShort, multiples, cardsInfo4Next, leftCount match {
        case 0 => PlayStatus.End.getCode
        case _ => PlayStatus.WaitingOtherPlay.getCode
      }, now)
      _ <- TmSeat.filter(_.id === nextSeatId).map(s => (s.previousCardsCount, s.multiples, s.proCardsInfo, s.playStatus, s.seqInGame, s.gmtUpdate)).update(leftCount.toShort, multiples, cardsInfo4Next, leftCount match {
        case 0 => PlayStatus.End.getCode
        case _ => PlayStatus.TurnToPlay.getCode
      }, activeSeqInGame.toShort, now)
      _ <- TmPlayRecord += playRecordRow
    } yield ()).transactionally
    Await.result(db.run(tran), Duration.Inf)
  }

  def setGameStatus(gameId: Long, gameStatus: GameStatus) = db.run {
    TmGame.filter(_.id === gameId).map(g => (g.status, g.gmtUpdate)).update(gameStatus.getCode, new Timestamp(System.currentTimeMillis()))
  }
}

class TowVsOneRepositoryImpl extends TowVsOneRepository with DBComponent