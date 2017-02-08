package com.jxjxgo.gamecenter.repo

import com.jxjxgo.mysql.connection.DBImpl

/** Entity class storing rows of table TmGame
  *
  * @param id           Database column id SqlType(BIGINT), PrimaryKey
  * @param gameType     Database column game_type SqlType(INT), Default(0)
  * @param status       Database column status SqlType(TINYINT), Default(1)
  * @param player1Id    Database column player1_id SqlType(BIGINT), Default(0)
  * @param player2Id    Database column player2_id SqlType(BIGINT), Default(0)
  * @param player3Id    Database column player3_id SqlType(BIGINT), Default(0)
  * @param player1Cards Database column player1_cards SqlType(VARCHAR), Length(60,true), Default()
  * @param player2Cards Database column player2_cards SqlType(VARCHAR), Length(60,true), Default()
  * @param player3Cards Database column player3_cards SqlType(VARCHAR), Length(60,true), Default()
  * @param dzCards      Database column dz_cards SqlType(VARCHAR), Length(3,true), Default()
  * @param dzIdx        Database column dz_idx SqlType(TINYINT), Default(1)
  * @param gmtCreate    Database column gmt_create SqlType(TIMESTAMP)
  * @param gmtUpdate    Database column gmt_update SqlType(TIMESTAMP) */
case class TmGameRow(id: Long, gameType: Int = 0, status: Byte = 1, player1Id: Long = 0L, player2Id: Long = 0L, player3Id: Long = 0L, player1Cards: String = "", player2Cards: String = "", player3Cards: String = "", dzCards: String = "", dzIdx: Byte = 1, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmPlayCards
  *
  * @param id         Database column id SqlType(BIGINT), PrimaryKey
  * @param gameId     Database column game_id SqlType(BIGINT)
  * @param seqInGame  Database column seq_in_game SqlType(TINYINT), Default(0)
  * @param `type`     Database column type SqlType(TINYINT), Default(0)
  * @param cards      Database column cards SqlType(VARCHAR), Length(13,true), Default()
  * @param cardsType  Database column cards_type SqlType(TINYINT), Default(0)
  * @param seqInRound Database column seq_in_round SqlType(TINYINT), Default(0) */
case class TmPlayCardsRow(id: Long, gameId: Long, seqInGame: Byte = 0, `type`: Byte = 0, cards: String = "", cardsType: Byte = 0, seqInRound: Byte = 0)

/** Entity class storing rows of table TmRoom
  *
  * @param id             Database column id SqlType(BIGINT), AutoInc, PrimaryKey
  * @param name           Database column name SqlType(VARCHAR), Length(64,true), Default()
  * @param status         Database column status SqlType(TINYINT), Default(1)
  * @param gameType       Database column game_type SqlType(TINYINT), Default(0)
  * @param baseAmount     Database column base_amount SqlType(DECIMAL), Default(0.00)
  * @param minTableId     Database column min_table_id SqlType(BIGINT), Default(0)
  * @param maxTableId     Database column max_table_id SqlType(BIGINT), Default(0)
  * @param seatCapacity   Database column seat_capacity SqlType(BIGINT), Default(0)
  * @param currentPlayers Database column current_players SqlType(BIGINT), Default(0)
  * @param gmtCreate      Database column gmt_create SqlType(TIMESTAMP)
  * @param gmtUpdate      Database column gmt_update SqlType(TIMESTAMP) */
case class TmRoomRow(id: Long, name: String = "", status: Byte = 1, gameType: Byte = 0, baseAmount: scala.math.BigDecimal = scala.math.BigDecimal(0), minTableId: Long = 0L, maxTableId: Long = 0L, seatCapacity: Long = 0L, currentPlayers: Long = 0L, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmRoomConfig
  *
  * @param gameType   Database column game_type SqlType(INT), PrimaryKey
  * @param roomRanges Database column room_ranges SqlType(VARCHAR), Length(64,true), Default()
  * @param gmtCreate  Database column gmt_create SqlType(TIMESTAMP)
  * @param gmtUpdate  Database column gmt_update SqlType(TIMESTAMP) */
case class TmRoomConfigRow(gameType: Int, roomRanges: String = "", gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmSeat
  *  @param id Database column id SqlType(BIGINT), PrimaryKey
  *  @param traceId Database column trace_id SqlType(VARCHAR), Length(64,true)
  *  @param status Database column status SqlType(TINYINT), Default(0)
  *  @param memberId Database column member_id SqlType(BIGINT), Default(0)
  *  @param gmtCreate Database column gmt_create SqlType(TIMESTAMP)
  *  @param gmtUpdate Database column gmt_update SqlType(TIMESTAMP) */
case class TmSeatRow(id: Long, traceId: String, status: Byte = 0, memberId: Long = 0L, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmTable
  *
  * @param id             Database column id SqlType(BIGINT), PrimaryKey
  * @param name           Database column name SqlType(VARCHAR), Length(64,true), Default()
  * @param status         Database column status SqlType(TINYINT), Default(0)
  * @param roomCapacity   Database column room_capacity SqlType(BIGINT), Default(0)
  * @param minSeatId      Database column min_seat_id SqlType(BIGINT), Default(0)
  * @param maxSeatId      Database column max_seat_id SqlType(BIGINT), Default(0)
  * @param currentPlayers Database column current_players SqlType(TINYINT), Default(0)
  * @param gmtCreate      Database column gmt_create SqlType(TIMESTAMP)
  * @param gmtUpdate      Database column gmt_update SqlType(TIMESTAMP) */
case class TmTableRow(id: Long, name: String = "", status: Byte = 0, roomCapacity: Long = 0L, minSeatId: Long = 0L, maxSeatId: Long = 0L, currentPlayers: Byte = 0, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables extends DBImpl {
  import profile.api._

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** GetResult implicit for fetching TmGameRow objects using plain SQL queries */
  implicit def GetResultTmGameRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Byte], e3: GR[String], e4: GR[java.sql.Timestamp]): GR[TmGameRow] = GR {
    prs => import prs._
      TmGameRow.tupled((<<[Long], <<[Int], <<[Byte], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[Byte], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_game. Objects of this class serve as prototypes for rows in queries. */
  class TmGame(_tableTag: Tag) extends profile.api.Table[TmGameRow](_tableTag, Some("ddz"), "tm_game") {
    def * = (id, gameType, status, player1Id, player2Id, player3Id, player1Cards, player2Cards, player3Cards, dzCards, dzIdx, gmtCreate, gmtUpdate) <> (TmGameRow.tupled, TmGameRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(gameType), Rep.Some(status), Rep.Some(player1Id), Rep.Some(player2Id), Rep.Some(player3Id), Rep.Some(player1Cards), Rep.Some(player2Cards), Rep.Some(player3Cards), Rep.Some(dzCards), Rep.Some(dzIdx), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmGameRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column game_type SqlType(INT), Default(0) */
    val gameType: Rep[Int] = column[Int]("game_type", O.Default(0))
    /** Database column status SqlType(TINYINT), Default(1) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(1))
    /** Database column player1_id SqlType(BIGINT), Default(0) */
    val player1Id: Rep[Long] = column[Long]("player1_id", O.Default(0L))
    /** Database column player2_id SqlType(BIGINT), Default(0) */
    val player2Id: Rep[Long] = column[Long]("player2_id", O.Default(0L))
    /** Database column player3_id SqlType(BIGINT), Default(0) */
    val player3Id: Rep[Long] = column[Long]("player3_id", O.Default(0L))
    /** Database column player1_cards SqlType(VARCHAR), Length(60,true), Default() */
    val player1Cards: Rep[String] = column[String]("player1_cards", O.Length(60, varying = true), O.Default(""))
    /** Database column player2_cards SqlType(VARCHAR), Length(60,true), Default() */
    val player2Cards: Rep[String] = column[String]("player2_cards", O.Length(60, varying = true), O.Default(""))
    /** Database column player3_cards SqlType(VARCHAR), Length(60,true), Default() */
    val player3Cards: Rep[String] = column[String]("player3_cards", O.Length(60, varying = true), O.Default(""))
    /** Database column dz_cards SqlType(VARCHAR), Length(3,true), Default() */
    val dzCards: Rep[String] = column[String]("dz_cards", O.Length(3, varying = true), O.Default(""))
    /** Database column dz_idx SqlType(TINYINT), Default(1) */
    val dzIdx: Rep[Byte] = column[Byte]("dz_idx", O.Default(1))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }

  /** Collection-like TableQuery object for table TmGame */
  lazy val TmGame = new TableQuery(tag => new TmGame(tag))

  /** GetResult implicit for fetching TmPlayCardsRow objects using plain SQL queries */
  implicit def GetResultTmPlayCardsRow(implicit e0: GR[Long], e1: GR[Byte], e2: GR[String]): GR[TmPlayCardsRow] = GR {
    prs => import prs._
      TmPlayCardsRow.tupled((<<[Long], <<[Long], <<[Byte], <<[Byte], <<[String], <<[Byte], <<[Byte]))
  }

  /** Table description of table tm_play_cards. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type */
  class TmPlayCards(_tableTag: Tag) extends profile.api.Table[TmPlayCardsRow](_tableTag, Some("ddz"), "tm_play_cards") {
    def * = (id, gameId, seqInGame, `type`, cards, cardsType, seqInRound) <> (TmPlayCardsRow.tupled, TmPlayCardsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(gameId), Rep.Some(seqInGame), Rep.Some(`type`), Rep.Some(cards), Rep.Some(cardsType), Rep.Some(seqInRound)).shaped.<>({ r => import r._; _1.map(_ => TmPlayCardsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column game_id SqlType(BIGINT) */
    val gameId: Rep[Long] = column[Long]("game_id")
    /** Database column seq_in_game SqlType(TINYINT), Default(0) */
    val seqInGame: Rep[Byte] = column[Byte]("seq_in_game", O.Default(0))
    /** Database column type SqlType(TINYINT), Default(0)
      * NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Byte] = column[Byte]("type", O.Default(0))
    /** Database column cards SqlType(VARCHAR), Length(13,true), Default() */
    val cards: Rep[String] = column[String]("cards", O.Length(13, varying = true), O.Default(""))
    /** Database column cards_type SqlType(TINYINT), Default(0) */
    val cardsType: Rep[Byte] = column[Byte]("cards_type", O.Default(0))
    /** Database column seq_in_round SqlType(TINYINT), Default(0) */
    val seqInRound: Rep[Byte] = column[Byte]("seq_in_round", O.Default(0))
  }

  /** Collection-like TableQuery object for table TmPlayCards */
  lazy val TmPlayCards = new TableQuery(tag => new TmPlayCards(tag))

  /** GetResult implicit for fetching TmRoomRow objects using plain SQL queries */
  implicit def GetResultTmRoomRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[scala.math.BigDecimal], e4: GR[java.sql.Timestamp]): GR[TmRoomRow] = GR {
    prs => import prs._
      TmRoomRow.tupled((<<[Long], <<[String], <<[Byte], <<[Byte], <<[scala.math.BigDecimal], <<[Long], <<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_room. Objects of this class serve as prototypes for rows in queries. */
  class TmRoom(_tableTag: Tag) extends profile.api.Table[TmRoomRow](_tableTag, "tm_room") {
    def * = (id, name, status, gameType, baseAmount, minTableId, maxTableId, seatCapacity, currentPlayers, gmtCreate, gmtUpdate) <> (TmRoomRow.tupled, TmRoomRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status), Rep.Some(gameType), Rep.Some(baseAmount), Rep.Some(minTableId), Rep.Some(maxTableId), Rep.Some(seatCapacity), Rep.Some(currentPlayers), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmRoomRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(64,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(64, varying = true), O.Default(""))
    /** Database column status SqlType(TINYINT), Default(1) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(1))
    /** Database column game_type SqlType(TINYINT), Default(0) */
    val gameType: Rep[Byte] = column[Byte]("game_type", O.Default(0))
    /** Database column base_amount SqlType(DECIMAL), Default(0.00) */
    val baseAmount: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("base_amount", O.Default(scala.math.BigDecimal(0)))
    /** Database column min_table_id SqlType(BIGINT), Default(0) */
    val minTableId: Rep[Long] = column[Long]("min_table_id", O.Default(0L))
    /** Database column max_table_id SqlType(BIGINT), Default(0) */
    val maxTableId: Rep[Long] = column[Long]("max_table_id", O.Default(0L))
    /** Database column seat_capacity SqlType(BIGINT), Default(0) */
    val seatCapacity: Rep[Long] = column[Long]("seat_capacity", O.Default(0L))
    /** Database column current_players SqlType(BIGINT), Default(0) */
    val currentPlayers: Rep[Long] = column[Long]("current_players", O.Default(0L))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")

    /** Index over (status) (database name idx_sta) */
    val index1 = index("idx_sta", status)
  }

  /** Collection-like TableQuery object for table TmRoom */
  lazy val TmRoom = new TableQuery(tag => new TmRoom(tag))

  /** GetResult implicit for fetching TmRoomConfigRow objects using plain SQL queries */
  implicit def GetResultTmRoomConfigRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TmRoomConfigRow] = GR {
    prs => import prs._
      TmRoomConfigRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_room_config. Objects of this class serve as prototypes for rows in queries. */
  class TmRoomConfig(_tableTag: Tag) extends profile.api.Table[TmRoomConfigRow](_tableTag, "tm_room_config") {
    def * = (gameType, roomRanges, gmtCreate, gmtUpdate) <> (TmRoomConfigRow.tupled, TmRoomConfigRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(gameType), Rep.Some(roomRanges), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmRoomConfigRow.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column game_type SqlType(INT), PrimaryKey */
    val gameType: Rep[Int] = column[Int]("game_type", O.PrimaryKey)
    /** Database column room_ranges SqlType(VARCHAR), Length(64,true), Default() */
    val roomRanges: Rep[String] = column[String]("room_ranges", O.Length(64, varying = true), O.Default(""))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }

  /** Collection-like TableQuery object for table TmRoomConfig */
  lazy val TmRoomConfig = new TableQuery(tag => new TmRoomConfig(tag))

  /** GetResult implicit for fetching TmSeatRow objects using plain SQL queries */
  implicit def GetResultTmSeatRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp]): GR[TmSeatRow] = GR{
    prs => import prs._
      TmSeatRow.tupled((<<[Long], <<[String], <<[Byte], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_seat. Objects of this class serve as prototypes for rows in queries. */
  class TmSeat(_tableTag: Tag) extends profile.api.Table[TmSeatRow](_tableTag, "tm_seat") {
    def * = (id, traceId, status, memberId, gmtCreate, gmtUpdate) <> (TmSeatRow.tupled, TmSeatRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(traceId), Rep.Some(status), Rep.Some(memberId), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({r=>import r._; _1.map(_=> TmSeatRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column trace_id SqlType(VARCHAR), Length(64,true) */
    val traceId: Rep[String] = column[String]("trace_id", O.Length(64,varying=true))
    /** Database column status SqlType(TINYINT), Default(0) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(0))
    /** Database column member_id SqlType(BIGINT), Default(0) */
    val memberId: Rep[Long] = column[Long]("member_id", O.Default(0L))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")

    /** Index over (status) (database name idx_sta) */
    val index1 = index("idx_sta", status)
  }
  /** Collection-like TableQuery object for table TmSeat */
  lazy val TmSeat = new TableQuery(tag => new TmSeat(tag))

  /** GetResult implicit for fetching TmTableRow objects using plain SQL queries */
  implicit def GetResultTmTableRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[java.sql.Timestamp]): GR[TmTableRow] = GR {
    prs => import prs._
      TmTableRow.tupled((<<[Long], <<[String], <<[Byte], <<[Long], <<[Long], <<[Long], <<[Byte], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_table. Objects of this class serve as prototypes for rows in queries. */
  class TmTable(_tableTag: Tag) extends profile.api.Table[TmTableRow](_tableTag, "tm_table") {
    def * = (id, name, status, roomCapacity, minSeatId, maxSeatId, currentPlayers, gmtCreate, gmtUpdate) <> (TmTableRow.tupled, TmTableRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status), Rep.Some(roomCapacity), Rep.Some(minSeatId), Rep.Some(maxSeatId), Rep.Some(currentPlayers), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmTableRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(64,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(64, varying = true), O.Default(""))
    /** Database column status SqlType(TINYINT), Default(0) */
    val status: Rep[Byte] = column[Byte]("status", O.Default(0))
    /** Database column room_capacity SqlType(BIGINT), Default(0) */
    val roomCapacity: Rep[Long] = column[Long]("room_capacity", O.Default(0L))
    /** Database column min_seat_id SqlType(BIGINT), Default(0) */
    val minSeatId: Rep[Long] = column[Long]("min_seat_id", O.Default(0L))
    /** Database column max_seat_id SqlType(BIGINT), Default(0) */
    val maxSeatId: Rep[Long] = column[Long]("max_seat_id", O.Default(0L))
    /** Database column current_players SqlType(TINYINT), Default(0) */
    val currentPlayers: Rep[Byte] = column[Byte]("current_players", O.Default(0))
    /** Database column gmt_create SqlType(TIMESTAMP) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(TIMESTAMP) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")

    /** Index over (status) (database name idx_sta) */
    val index1 = index("idx_sta", status)
  }

  /** Collection-like TableQuery object for table TmTable */
  lazy val TmTable = new TableQuery(tag => new TmTable(tag))
}
