package com.jxjxgo.gamecenter.repo

import com.jxjxgo.mysql.connection.DBImpl

/** Entity class storing rows of table TmChannelAddress
  *
  * @param memberId  Database column member_id SqlType(int8), PrimaryKey
  * @param host      Database column host SqlType(varchar), Length(32,true)
  * @param `type`    Database column type SqlType(varchar), Length(255,true), Default(rpc)
  * @param gmtCreate Database column gmt_create SqlType(timestamp)
  * @param gmtUpdate Database column gmt_update SqlType(timestamp) */
case class TmChannelAddressRow(memberId: Long, host: String, `type`: String = "rpc", gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmGame
  *
  * @param id           Database column id SqlType(int8), PrimaryKey
  * @param gameType     Database column game_type SqlType(int4), Default(0)
  * @param status       Database column status SqlType(int2)
  * @param player1Id    Database column player1_id SqlType(int8), Default(0)
  * @param player2Id    Database column player2_id SqlType(int8), Default(0)
  * @param player3Id    Database column player3_id SqlType(int8), Default(0)
  * @param player1Cards Database column player1_cards SqlType(varchar), Length(60,true), Default()
  * @param player2Cards Database column player2_cards SqlType(varchar), Length(60,true), Default()
  * @param player3Cards Database column player3_cards SqlType(varchar), Length(60,true), Default()
  * @param dzCards      Database column dz_cards SqlType(varchar), Length(11,true), Default()
  * @param dzIdx        Database column dz_idx SqlType(int2)
  * @param gmtCreate    Database column gmt_create SqlType(timestamp)
  * @param gmtUpdate    Database column gmt_update SqlType(timestamp) */
case class TmGameRow(id: Long, gameType: Int = 0, status: Short, player1Id: Long = 0L, player2Id: Long = 0L, player3Id: Long = 0L, player1Cards: String = "", player2Cards: String = "", player3Cards: String = "", dzCards: String = "", dzIdx: Short, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmOnlineRecord
  *
  * @param socketUuid  Database column socket_uuid SqlType(varchar), PrimaryKey, Length(32,true)
  * @param deviceType  Database column device_type SqlType(int2)
  * @param fingerPrint Database column finger_print SqlType(varchar), Length(64,true)
  * @param memberId    Database column member_id SqlType(int8)
  * @param ip          Database column ip SqlType(int8)
  * @param rpcHost     Database column rpc_host SqlType(varchar), Length(32,true)
  * @param gmtOnline   Database column gmt_online SqlType(timestamp)
  * @param gmtOffline  Database column gmt_offline SqlType(timestamp), Default(None)
  * @param online      Database column online SqlType(bool) */
case class TmOnlineRecordRow(socketUuid: String, deviceType: Short, fingerPrint: String, memberId: Long, ip: Long, rpcHost: String, gmtOnline: java.sql.Timestamp, gmtOffline: Option[java.sql.Timestamp] = None, online: Boolean)

/** Entity class storing rows of table TmPlayCards
  *
  * @param id         Database column id SqlType(int8), PrimaryKey
  * @param gameId     Database column game_id SqlType(int8)
  * @param seqInGame  Database column seq_in_game SqlType(int2)
  * @param `type`     Database column type SqlType(int2)
  * @param cards      Database column cards SqlType(varchar), Length(13,true), Default()
  * @param cardsType  Database column cards_type SqlType(int2)
  * @param seqInRound Database column seq_in_round SqlType(int2) */
case class TmPlayCardsRow(id: Long, gameId: Long, seqInGame: Short, `type`: Short, cards: String = "", cardsType: Short, seqInRound: Short)

/** Entity class storing rows of table TmRoom
  *
  * @param id             Database column id SqlType(int8), PrimaryKey
  * @param name           Database column name SqlType(varchar), Length(64,true), Default()
  * @param status         Database column status SqlType(int2)
  * @param gameType       Database column game_type SqlType(int2)
  * @param baseAmount     Database column base_amount SqlType(numeric), Default(0.00)
  * @param minTableId     Database column min_table_id SqlType(int8), Default(0)
  * @param maxTableId     Database column max_table_id SqlType(int8), Default(0)
  * @param seatCapacity   Database column seat_capacity SqlType(int8), Default(0)
  * @param currentPlayers Database column current_players SqlType(int8), Default(0)
  * @param gmtCreate      Database column gmt_create SqlType(timestamp)
  * @param gmtUpdate      Database column gmt_update SqlType(timestamp) */
case class TmRoomRow(id: Long, name: String = "", status: Short, gameType: Short, baseAmount: scala.math.BigDecimal = scala.math.BigDecimal("0.00"), minTableId: Long = 0L, maxTableId: Long = 0L, seatCapacity: Long = 0L, currentPlayers: Long = 0L, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmRoomConfig
  *
  * @param gameType   Database column game_type SqlType(int4), PrimaryKey
  * @param roomRanges Database column room_ranges SqlType(varchar), Length(64,true), Default()
  * @param gmtCreate  Database column gmt_create SqlType(timestamp)
  * @param gmtUpdate  Database column gmt_update SqlType(timestamp) */
case class TmRoomConfigRow(gameType: Int, roomRanges: String = "", gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Entity class storing rows of table TmSeat
  *
  * @param id                 Database column id SqlType(int8), PrimaryKey
  * @param traceId            Database column trace_id SqlType(varchar), Length(64,true)
  * @param status             Database column status SqlType(int2)
  * @param memberId           Database column member_id SqlType(int8), Default(0)
  * @param cards              Database column cards SqlType(varchar), Length(60,true)
  * @param ip                 Database column ip SqlType(int8)
  * @param fingerPrint        Database column finger_print SqlType(varchar), Length(64,true)
  * @param gmtCreate          Database column gmt_create SqlType(timestamp)
  * @param gmtUpdate          Database column gmt_update SqlType(timestamp)
  * @param gameId             Database column game_id SqlType(int8)
  * @param gameType           Database column game_type SqlType(varchar), Length(32,true)
  * @param multiples          Database column multiples SqlType(int4)
  * @param baseAmount         Database column base_amount SqlType(int4)
  * @param previousNickname   Database column previous_nickname SqlType(varchar), Length(64,true)
  * @param previousCardsCount Database column previous_cards_count SqlType(int2)
  * @param nextNickname       Database column next_nickname SqlType(varchar), Length(64,true)
  * @param nextCardsCount     Database column next_cards_count SqlType(int2)
  * @param choosingLandlord   Database column choosing_landlord SqlType(bool)
  * @param landlord           Database column landlord SqlType(bool)
  * @param turnToPlay         Database column turn_to_play SqlType(bool) */
case class TmSeatRow(id: Long, traceId: String, status: Short, memberId: Long = 0L, cards: String, ip: Long, fingerPrint: String, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp, gameId: Long, gameType: String, multiples: Int, baseAmount: Int, previousNickname: String, previousCardsCount: Short, nextNickname: String, nextCardsCount: Short, choosingLandlord: Boolean, landlord: Boolean, turnToPlay: Boolean)

/** Entity class storing rows of table TmTable
  *
  * @param id             Database column id SqlType(int8), PrimaryKey
  * @param name           Database column name SqlType(varchar), Length(64,true), Default()
  * @param status         Database column status SqlType(int2)
  * @param roomCapacity   Database column room_capacity SqlType(int8), Default(0)
  * @param minSeatId      Database column min_seat_id SqlType(int8), Default(0)
  * @param maxSeatId      Database column max_seat_id SqlType(int8), Default(0)
  * @param currentPlayers Database column current_players SqlType(int2)
  * @param gmtCreate      Database column gmt_create SqlType(timestamp)
  * @param gmtUpdate      Database column gmt_update SqlType(timestamp) */
case class TmTableRow(id: Long, name: String = "", status: Short, roomCapacity: Long = 0L, minSeatId: Long = 0L, maxSeatId: Long = 0L, currentPlayers: Short, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables extends DBImpl {

  import profile.api._

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** GetResult implicit for fetching TmChannelAddressRow objects using plain SQL queries */
  implicit def GetResultTmChannelAddressRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TmChannelAddressRow] = GR {
    prs => import prs._
      TmChannelAddressRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_channel_address. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type */
  class TmChannelAddress(_tableTag: Tag) extends profile.api.Table[TmChannelAddressRow](_tableTag, "tm_channel_address") {
    def * = (memberId, host, `type`, gmtCreate, gmtUpdate) <> (TmChannelAddressRow.tupled, TmChannelAddressRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(host), Rep.Some(`type`), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmChannelAddressRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_id SqlType(int8), PrimaryKey */
    val memberId: Rep[Long] = column[Long]("member_id", O.PrimaryKey)
    /** Database column host SqlType(varchar), Length(32,true) */
    val host: Rep[String] = column[String]("host", O.Length(32, varying = true))
    /** Database column type SqlType(varchar), Length(255,true), Default(rpc)
      * NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(255, varying = true), O.Default("rpc"))
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }

  /** Collection-like TableQuery object for table TmChannelAddress */
  lazy val TmChannelAddress = new TableQuery(tag => new TmChannelAddress(tag))

  /** GetResult implicit for fetching TmGameRow objects using plain SQL queries */
  implicit def GetResultTmGameRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Short], e3: GR[String], e4: GR[java.sql.Timestamp]): GR[TmGameRow] = GR {
    prs => import prs._
      TmGameRow.tupled((<<[Long], <<[Int], <<[Short], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[Short], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_game. Objects of this class serve as prototypes for rows in queries. */
  class TmGame(_tableTag: Tag) extends profile.api.Table[TmGameRow](_tableTag, "tm_game") {
    def * = (id, gameType, status, player1Id, player2Id, player3Id, player1Cards, player2Cards, player3Cards, dzCards, dzIdx, gmtCreate, gmtUpdate) <> (TmGameRow.tupled, TmGameRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(gameType), Rep.Some(status), Rep.Some(player1Id), Rep.Some(player2Id), Rep.Some(player3Id), Rep.Some(player1Cards), Rep.Some(player2Cards), Rep.Some(player3Cards), Rep.Some(dzCards), Rep.Some(dzIdx), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmGameRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column game_type SqlType(int4), Default(0) */
    val gameType: Rep[Int] = column[Int]("game_type", O.Default(0))
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column player1_id SqlType(int8), Default(0) */
    val player1Id: Rep[Long] = column[Long]("player1_id", O.Default(0L))
    /** Database column player2_id SqlType(int8), Default(0) */
    val player2Id: Rep[Long] = column[Long]("player2_id", O.Default(0L))
    /** Database column player3_id SqlType(int8), Default(0) */
    val player3Id: Rep[Long] = column[Long]("player3_id", O.Default(0L))
    /** Database column player1_cards SqlType(varchar), Length(60,true), Default() */
    val player1Cards: Rep[String] = column[String]("player1_cards", O.Length(60, varying = true), O.Default(""))
    /** Database column player2_cards SqlType(varchar), Length(60,true), Default() */
    val player2Cards: Rep[String] = column[String]("player2_cards", O.Length(60, varying = true), O.Default(""))
    /** Database column player3_cards SqlType(varchar), Length(60,true), Default() */
    val player3Cards: Rep[String] = column[String]("player3_cards", O.Length(60, varying = true), O.Default(""))
    /** Database column dz_cards SqlType(varchar), Length(11,true), Default() */
    val dzCards: Rep[String] = column[String]("dz_cards", O.Length(11, varying = true), O.Default(""))
    /** Database column dz_idx SqlType(int2) */
    val dzIdx: Rep[Short] = column[Short]("dz_idx")
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }

  /** Collection-like TableQuery object for table TmGame */
  lazy val TmGame = new TableQuery(tag => new TmGame(tag))

  /** GetResult implicit for fetching TmPlayCardsRow objects using plain SQL queries */
  implicit def GetResultTmPlayCardsRow(implicit e0: GR[Long], e1: GR[Short], e2: GR[String]): GR[TmPlayCardsRow] = GR {
    prs => import prs._
      TmPlayCardsRow.tupled((<<[Long], <<[Long], <<[Short], <<[Short], <<[String], <<[Short], <<[Short]))
  }

  /** Table description of table tm_play_cards. Objects of this class serve as prototypes for rows in queries.
    * NOTE: The following names collided with Scala keywords and were escaped: type */
  class TmPlayCards(_tableTag: Tag) extends profile.api.Table[TmPlayCardsRow](_tableTag, "tm_play_cards") {
    def * = (id, gameId, seqInGame, `type`, cards, cardsType, seqInRound) <> (TmPlayCardsRow.tupled, TmPlayCardsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(gameId), Rep.Some(seqInGame), Rep.Some(`type`), Rep.Some(cards), Rep.Some(cardsType), Rep.Some(seqInRound)).shaped.<>({ r => import r._; _1.map(_ => TmPlayCardsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column game_id SqlType(int8) */
    val gameId: Rep[Long] = column[Long]("game_id")
    /** Database column seq_in_game SqlType(int2) */
    val seqInGame: Rep[Short] = column[Short]("seq_in_game")
    /** Database column type SqlType(int2)
      * NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Short] = column[Short]("type")
    /** Database column cards SqlType(varchar), Length(13,true), Default() */
    val cards: Rep[String] = column[String]("cards", O.Length(13, varying = true), O.Default(""))
    /** Database column cards_type SqlType(int2) */
    val cardsType: Rep[Short] = column[Short]("cards_type")
    /** Database column seq_in_round SqlType(int2) */
    val seqInRound: Rep[Short] = column[Short]("seq_in_round")
  }

  /** Collection-like TableQuery object for table TmPlayCards */
  lazy val TmPlayCards = new TableQuery(tag => new TmPlayCards(tag))

  /** GetResult implicit for fetching TmRoomRow objects using plain SQL queries */
  implicit def GetResultTmRoomRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Short], e3: GR[scala.math.BigDecimal], e4: GR[java.sql.Timestamp]): GR[TmRoomRow] = GR {
    prs => import prs._
      TmRoomRow.tupled((<<[Long], <<[String], <<[Short], <<[Short], <<[scala.math.BigDecimal], <<[Long], <<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_room. Objects of this class serve as prototypes for rows in queries. */
  class TmRoom(_tableTag: Tag) extends profile.api.Table[TmRoomRow](_tableTag, "tm_room") {
    def * = (id, name, status, gameType, baseAmount, minTableId, maxTableId, seatCapacity, currentPlayers, gmtCreate, gmtUpdate) <> (TmRoomRow.tupled, TmRoomRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status), Rep.Some(gameType), Rep.Some(baseAmount), Rep.Some(minTableId), Rep.Some(maxTableId), Rep.Some(seatCapacity), Rep.Some(currentPlayers), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmRoomRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(64,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(64, varying = true), O.Default(""))
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column game_type SqlType(int2) */
    val gameType: Rep[Short] = column[Short]("game_type")
    /** Database column base_amount SqlType(numeric), Default(0.00) */
    val baseAmount: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("base_amount", O.Default(scala.math.BigDecimal("0.00")))
    /** Database column min_table_id SqlType(int8), Default(0) */
    val minTableId: Rep[Long] = column[Long]("min_table_id", O.Default(0L))
    /** Database column max_table_id SqlType(int8), Default(0) */
    val maxTableId: Rep[Long] = column[Long]("max_table_id", O.Default(0L))
    /** Database column seat_capacity SqlType(int8), Default(0) */
    val seatCapacity: Rep[Long] = column[Long]("seat_capacity", O.Default(0L))
    /** Database column current_players SqlType(int8), Default(0) */
    val currentPlayers: Rep[Long] = column[Long]("current_players", O.Default(0L))
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
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

    /** Database column game_type SqlType(int4), PrimaryKey */
    val gameType: Rep[Int] = column[Int]("game_type", O.PrimaryKey)
    /** Database column room_ranges SqlType(varchar), Length(64,true), Default() */
    val roomRanges: Rep[String] = column[String]("room_ranges", O.Length(64, varying = true), O.Default(""))
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }

  /** Collection-like TableQuery object for table TmRoomConfig */
  lazy val TmRoomConfig = new TableQuery(tag => new TmRoomConfig(tag))

  /** GetResult implicit for fetching TmSeatRow objects using plain SQL queries */
  implicit def GetResultTmSeatRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Short], e3: GR[java.sql.Timestamp], e4: GR[Int], e5: GR[Boolean]): GR[TmSeatRow] = GR {
    prs => import prs._
      TmSeatRow.tupled((<<[Long], <<[String], <<[Short], <<[Long], <<[String], <<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long], <<[String], <<[Int], <<[Int], <<[String], <<[Short], <<[String], <<[Short], <<[Boolean], <<[Boolean], <<[Boolean]))
  }

  /** Table description of table tm_seat. Objects of this class serve as prototypes for rows in queries. */
  class TmSeat(_tableTag: Tag) extends profile.api.Table[TmSeatRow](_tableTag, "tm_seat") {
    def * = (id, traceId, status, memberId, cards, ip, fingerPrint, gmtCreate, gmtUpdate, gameId, gameType, multiples, baseAmount, previousNickname, previousCardsCount, nextNickname, nextCardsCount, choosingLandlord, landlord, turnToPlay) <> (TmSeatRow.tupled, TmSeatRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(traceId), Rep.Some(status), Rep.Some(memberId), Rep.Some(cards), Rep.Some(ip), Rep.Some(fingerPrint), Rep.Some(gmtCreate), Rep.Some(gmtUpdate), Rep.Some(gameId), Rep.Some(gameType), Rep.Some(multiples), Rep.Some(baseAmount), Rep.Some(previousNickname), Rep.Some(previousCardsCount), Rep.Some(nextNickname), Rep.Some(nextCardsCount), Rep.Some(choosingLandlord), Rep.Some(landlord), Rep.Some(turnToPlay)).shaped.<>({ r => import r._; _1.map(_ => TmSeatRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get, _18.get, _19.get, _20.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column trace_id SqlType(varchar), Length(64,true) */
    val traceId: Rep[String] = column[String]("trace_id", O.Length(64, varying = true))
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column member_id SqlType(int8), Default(0) */
    val memberId: Rep[Long] = column[Long]("member_id", O.Default(0L))
    /** Database column cards SqlType(varchar), Length(60,true) */
    val cards: Rep[String] = column[String]("cards", O.Length(60, varying = true))
    /** Database column ip SqlType(int8) */
    val ip: Rep[Long] = column[Long]("ip")
    /** Database column finger_print SqlType(varchar), Length(64,true) */
    val fingerPrint: Rep[String] = column[String]("finger_print", O.Length(64, varying = true))
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
    /** Database column game_id SqlType(int8) */
    val gameId: Rep[Long] = column[Long]("game_id")
    /** Database column game_type SqlType(varchar), Length(32,true) */
    val gameType: Rep[String] = column[String]("game_type", O.Length(32, varying = true))
    /** Database column multiples SqlType(int4) */
    val multiples: Rep[Int] = column[Int]("multiples")
    /** Database column base_amount SqlType(int4) */
    val baseAmount: Rep[Int] = column[Int]("base_amount")
    /** Database column previous_nickname SqlType(varchar), Length(64,true) */
    val previousNickname: Rep[String] = column[String]("previous_nickname", O.Length(64, varying = true))
    /** Database column previous_cards_count SqlType(int2) */
    val previousCardsCount: Rep[Short] = column[Short]("previous_cards_count")
    /** Database column next_nickname SqlType(varchar), Length(64,true) */
    val nextNickname: Rep[String] = column[String]("next_nickname", O.Length(64, varying = true))
    /** Database column next_cards_count SqlType(int2) */
    val nextCardsCount: Rep[Short] = column[Short]("next_cards_count")
    /** Database column choosing_landlord SqlType(bool) */
    val choosingLandlord: Rep[Boolean] = column[Boolean]("choosing_landlord")
    /** Database column landlord SqlType(bool) */
    val landlord: Rep[Boolean] = column[Boolean]("landlord")
    /** Database column turn_to_play SqlType(bool) */
    val turnToPlay: Rep[Boolean] = column[Boolean]("turn_to_play")
  }

  /** Collection-like TableQuery object for table TmSeat */
  lazy val TmSeat = new TableQuery(tag => new TmSeat(tag))

  /** GetResult implicit for fetching TmTableRow objects using plain SQL queries */
  implicit def GetResultTmTableRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Short], e3: GR[java.sql.Timestamp]): GR[TmTableRow] = GR {
    prs => import prs._
      TmTableRow.tupled((<<[Long], <<[String], <<[Short], <<[Long], <<[Long], <<[Long], <<[Short], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }

  /** Table description of table tm_table. Objects of this class serve as prototypes for rows in queries. */
  class TmTable(_tableTag: Tag) extends profile.api.Table[TmTableRow](_tableTag, "tm_table") {
    def * = (id, name, status, roomCapacity, minSeatId, maxSeatId, currentPlayers, gmtCreate, gmtUpdate) <> (TmTableRow.tupled, TmTableRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status), Rep.Some(roomCapacity), Rep.Some(minSeatId), Rep.Some(maxSeatId), Rep.Some(currentPlayers), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({ r => import r._; _1.map(_ => TmTableRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(64,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(64, varying = true), O.Default(""))
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column room_capacity SqlType(int8), Default(0) */
    val roomCapacity: Rep[Long] = column[Long]("room_capacity", O.Default(0L))
    /** Database column min_seat_id SqlType(int8), Default(0) */
    val minSeatId: Rep[Long] = column[Long]("min_seat_id", O.Default(0L))
    /** Database column max_seat_id SqlType(int8), Default(0) */
    val maxSeatId: Rep[Long] = column[Long]("max_seat_id", O.Default(0L))
    /** Database column current_players SqlType(int2) */
    val currentPlayers: Rep[Short] = column[Short]("current_players")
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }

  /** Collection-like TableQuery object for table TmTable */
  lazy val TmTable = new TableQuery(tag => new TmTable(tag))

  /** GetResult implicit for fetching TmOnlineRecordRow objects using plain SQL queries */
  implicit def GetResultTmOnlineRecordRow(implicit e0: GR[String], e1: GR[Short], e2: GR[Long], e3: GR[java.sql.Timestamp], e4: GR[Option[java.sql.Timestamp]], e5: GR[Boolean]): GR[TmOnlineRecordRow] = GR {
    prs => import prs._
      TmOnlineRecordRow.tupled((<<[String], <<[Short], <<[String], <<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Boolean]))
  }

  /** Table description of table tm_online_record. Objects of this class serve as prototypes for rows in queries. */
  class TmOnlineRecord(_tableTag: Tag) extends profile.api.Table[TmOnlineRecordRow](_tableTag, "tm_online_record") {
    def * = (socketUuid, deviceType, fingerPrint, memberId, ip, rpcHost, gmtOnline, gmtOffline, online) <> (TmOnlineRecordRow.tupled, TmOnlineRecordRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(socketUuid), Rep.Some(deviceType), Rep.Some(fingerPrint), Rep.Some(memberId), Rep.Some(ip), Rep.Some(rpcHost), Rep.Some(gmtOnline), gmtOffline, Rep.Some(online)).shaped.<>({ r => import r._; _1.map(_ => TmOnlineRecordRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column socket_uuid SqlType(varchar), PrimaryKey, Length(32,true) */
    val socketUuid: Rep[String] = column[String]("socket_uuid", O.PrimaryKey, O.Length(32, varying = true))
    /** Database column device_type SqlType(int2) */
    val deviceType: Rep[Short] = column[Short]("device_type")
    /** Database column finger_print SqlType(varchar), Length(64,true) */
    val fingerPrint: Rep[String] = column[String]("finger_print", O.Length(64, varying = true))
    /** Database column member_id SqlType(int8) */
    val memberId: Rep[Long] = column[Long]("member_id")
    /** Database column ip SqlType(int8) */
    val ip: Rep[Long] = column[Long]("ip")
    /** Database column rpc_host SqlType(varchar), Length(32,true) */
    val rpcHost: Rep[String] = column[String]("rpc_host", O.Length(32, varying = true))
    /** Database column gmt_online SqlType(timestamp) */
    val gmtOnline: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_online")
    /** Database column gmt_offline SqlType(timestamp), Default(None) */
    val gmtOffline: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("gmt_offline", O.Default(None))
    /** Database column online SqlType(bool) */
    val online: Rep[Boolean] = column[Boolean]("online")
  }

  /** Collection-like TableQuery object for table TmOnlineRecord */
  lazy val TmOnlineRecord = new TableQuery(tag => new TmOnlineRecord(tag))
}
