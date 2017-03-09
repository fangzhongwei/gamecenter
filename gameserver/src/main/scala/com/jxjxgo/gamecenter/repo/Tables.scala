package com.jxjxgo.gamecenter.repo

import com.jxjxgo.mysql.connection.DBImpl

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables extends DBImpl {

  import profile.api._

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** Entity class storing rows of table TmGame
    *  @param id Database column id SqlType(int8), PrimaryKey
    *  @param gameType Database column game_type SqlType(int4), Default(0)
    *  @param deviceType Database column device_type SqlType(int4)
    *  @param baseAmount Database column base_amount SqlType(int4)
    *  @param multiples Database column multiples SqlType(int4)
    *  @param status Database column status SqlType(int2)
    *  @param player1Id Database column player1_id SqlType(int8), Default(0)
    *  @param player2Id Database column player2_id SqlType(int8), Default(0)
    *  @param player3Id Database column player3_id SqlType(int8), Default(0)
    *  @param seat1Id Database column seat1_id SqlType(int8), Default(0)
    *  @param seat2Id Database column seat2_id SqlType(int8), Default(0)
    *  @param seat3Id Database column seat3_id SqlType(int8), Default(0)
    *  @param seqInGame Database column seq_in_game SqlType(int4), Default(0)
    *  @param activePlayerId Database column active_player_id SqlType(int8), Default(0)
    *  @param player1Cards Database column player1_cards SqlType(varchar), Length(128,true), Default()
    *  @param player2Cards Database column player2_cards SqlType(varchar), Length(128,true), Default()
    *  @param player3Cards Database column player3_cards SqlType(varchar), Length(128,true), Default()
    *  @param outsideCards Database column outside_cards SqlType(varchar), Length(128,true), Default()
    *  @param landlordId Database column landlord_id SqlType(int8)
    *  @param winnerId Database column winner_id SqlType(int8)
    *  @param gmtCreate Database column gmt_create SqlType(timestamp)
    *  @param gmtUpdate Database column gmt_update SqlType(timestamp) */
  case class TmGameRow(id: Long, gameType: Int = 0, deviceType: Int, baseAmount: Int, multiples: Int, status: Short, player1Id: Long = 0L, player2Id: Long = 0L, player3Id: Long = 0L, seat1Id: Long = 0L, seat2Id: Long = 0L, seat3Id: Long = 0L, seqInGame: Int = 0, activePlayerId: Long = 0L, player1Cards: String = "", player2Cards: String = "", player3Cards: String = "", outsideCards: String = "", landlordId: Long, winnerId: Long, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)
  /** GetResult implicit for fetching TmGameRow objects using plain SQL queries */
  implicit def GetResultTmGameRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Short], e3: GR[String], e4: GR[java.sql.Timestamp]): GR[TmGameRow] = GR{
    prs => import prs._
      TmGameRow.tupled((<<[Long], <<[Int], <<[Int], <<[Int], <<[Int], <<[Short], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Long], <<[Int], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_game. Objects of this class serve as prototypes for rows in queries. */
  class TmGame(_tableTag: Tag) extends profile.api.Table[TmGameRow](_tableTag, "tm_game") {
    def * = (id, gameType, deviceType, baseAmount, multiples, status, player1Id, player2Id, player3Id, seat1Id, seat2Id, seat3Id, seqInGame, activePlayerId, player1Cards, player2Cards, player3Cards, outsideCards, landlordId, winnerId, gmtCreate, gmtUpdate) <> (TmGameRow.tupled, TmGameRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(gameType), Rep.Some(deviceType), Rep.Some(baseAmount), Rep.Some(multiples), Rep.Some(status), Rep.Some(player1Id), Rep.Some(player2Id), Rep.Some(player3Id), Rep.Some(seat1Id), Rep.Some(seat2Id), Rep.Some(seat3Id), Rep.Some(seqInGame), Rep.Some(activePlayerId), Rep.Some(player1Cards), Rep.Some(player2Cards), Rep.Some(player3Cards), Rep.Some(outsideCards), Rep.Some(landlordId), Rep.Some(winnerId), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({r=>import r._; _1.map(_=> TmGameRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get, _18.get, _19.get, _20.get, _21.get, _22.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column game_type SqlType(int4), Default(0) */
    val gameType: Rep[Int] = column[Int]("game_type", O.Default(0))
    /** Database column device_type SqlType(int4) */
    val deviceType: Rep[Int] = column[Int]("device_type")
    /** Database column base_amount SqlType(int4) */
    val baseAmount: Rep[Int] = column[Int]("base_amount")
    /** Database column multiples SqlType(int4) */
    val multiples: Rep[Int] = column[Int]("multiples")
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column player1_id SqlType(int8), Default(0) */
    val player1Id: Rep[Long] = column[Long]("player1_id", O.Default(0L))
    /** Database column player2_id SqlType(int8), Default(0) */
    val player2Id: Rep[Long] = column[Long]("player2_id", O.Default(0L))
    /** Database column player3_id SqlType(int8), Default(0) */
    val player3Id: Rep[Long] = column[Long]("player3_id", O.Default(0L))
    /** Database column seat1_id SqlType(int8), Default(0) */
    val seat1Id: Rep[Long] = column[Long]("seat1_id", O.Default(0L))
    /** Database column seat2_id SqlType(int8), Default(0) */
    val seat2Id: Rep[Long] = column[Long]("seat2_id", O.Default(0L))
    /** Database column seat3_id SqlType(int8), Default(0) */
    val seat3Id: Rep[Long] = column[Long]("seat3_id", O.Default(0L))
    /** Database column seq_in_game SqlType(int4), Default(0) */
    val seqInGame: Rep[Int] = column[Int]("seq_in_game", O.Default(0))
    /** Database column active_player_id SqlType(int8), Default(0) */
    val activePlayerId: Rep[Long] = column[Long]("active_player_id", O.Default(0L))
    /** Database column player1_cards SqlType(varchar), Length(128,true), Default() */
    val player1Cards: Rep[String] = column[String]("player1_cards", O.Length(128,varying=true), O.Default(""))
    /** Database column player2_cards SqlType(varchar), Length(128,true), Default() */
    val player2Cards: Rep[String] = column[String]("player2_cards", O.Length(128,varying=true), O.Default(""))
    /** Database column player3_cards SqlType(varchar), Length(128,true), Default() */
    val player3Cards: Rep[String] = column[String]("player3_cards", O.Length(128,varying=true), O.Default(""))
    /** Database column outside_cards SqlType(varchar), Length(128,true), Default() */
    val outsideCards: Rep[String] = column[String]("outside_cards", O.Length(128,varying=true), O.Default(""))
    /** Database column landlord_id SqlType(int8) */
    val landlordId: Rep[Long] = column[Long]("landlord_id")
    /** Database column winner_id SqlType(int8) */
    val winnerId: Rep[Long] = column[Long]("winner_id")
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }
  /** Collection-like TableQuery object for table TmGame */
  lazy val TmGame = new TableQuery(tag => new TmGame(tag))

  /** Entity class storing rows of table TmOnlineRecord
    *  @param socketId Database column socket_id SqlType(int8), PrimaryKey
    *  @param deviceType Database column device_type SqlType(int4)
    *  @param fingerPrint Database column finger_print SqlType(varchar), Length(64,true)
    *  @param memberId Database column member_id SqlType(int8)
    *  @param ip Database column ip SqlType(int8)
    *  @param rpcHost Database column rpc_host SqlType(varchar), Length(32,true)
    *  @param gmtOnline Database column gmt_online SqlType(timestamp)
    *  @param gmtOffline Database column gmt_offline SqlType(timestamp), Default(None)
    *  @param online Database column online SqlType(bool) */
  case class TmOnlineRecordRow(socketId: Long, deviceType: Int, fingerPrint: String, memberId: Long, ip: Long, rpcHost: String, gmtOnline: java.sql.Timestamp, gmtOffline: Option[java.sql.Timestamp] = None, online: Boolean)
  /** GetResult implicit for fetching TmOnlineRecordRow objects using plain SQL queries */
  implicit def GetResultTmOnlineRecordRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[String], e3: GR[java.sql.Timestamp], e4: GR[Option[java.sql.Timestamp]], e5: GR[Boolean]): GR[TmOnlineRecordRow] = GR{
    prs => import prs._
      TmOnlineRecordRow.tupled((<<[Long], <<[Int], <<[String], <<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Boolean]))
  }
  /** Table description of table tm_online_record. Objects of this class serve as prototypes for rows in queries. */
  class TmOnlineRecord(_tableTag: Tag) extends profile.api.Table[TmOnlineRecordRow](_tableTag, "tm_online_record") {
    def * = (socketId, deviceType, fingerPrint, memberId, ip, rpcHost, gmtOnline, gmtOffline, online) <> (TmOnlineRecordRow.tupled, TmOnlineRecordRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(socketId), Rep.Some(deviceType), Rep.Some(fingerPrint), Rep.Some(memberId), Rep.Some(ip), Rep.Some(rpcHost), Rep.Some(gmtOnline), gmtOffline, Rep.Some(online)).shaped.<>({r=>import r._; _1.map(_=> TmOnlineRecordRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column socket_id SqlType(int8), PrimaryKey */
    val socketId: Rep[Long] = column[Long]("socket_id", O.PrimaryKey)
    /** Database column device_type SqlType(int4) */
    val deviceType: Rep[Int] = column[Int]("device_type")
    /** Database column finger_print SqlType(varchar), Length(64,true) */
    val fingerPrint: Rep[String] = column[String]("finger_print", O.Length(64,varying=true))
    /** Database column member_id SqlType(int8) */
    val memberId: Rep[Long] = column[Long]("member_id")
    /** Database column ip SqlType(int8) */
    val ip: Rep[Long] = column[Long]("ip")
    /** Database column rpc_host SqlType(varchar), Length(32,true) */
    val rpcHost: Rep[String] = column[String]("rpc_host", O.Length(32,varying=true))
    /** Database column gmt_online SqlType(timestamp) */
    val gmtOnline: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_online")
    /** Database column gmt_offline SqlType(timestamp), Default(None) */
    val gmtOffline: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("gmt_offline", O.Default(None))
    /** Database column online SqlType(bool) */
    val online: Rep[Boolean] = column[Boolean]("online")
  }
  /** Collection-like TableQuery object for table TmOnlineRecord */
  lazy val TmOnlineRecord = new TableQuery(tag => new TmOnlineRecord(tag))

  /** Entity class storing rows of table TmPlayRecord
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param playType Database column play_type SqlType(int2)
    *  @param gameId Database column game_id SqlType(int8)
    *  @param memberId Database column member_id SqlType(int8)
    *  @param seqInGame Database column seq_in_game SqlType(int2)
    *  @param cardsType Database column cards_type SqlType(varchar), Length(32,true)
    *  @param keys Database column keys SqlType(varchar), Length(64,true)
    *  @param handPoints Database column hand_points SqlType(varchar), Length(128,true)
    *  @param playPoints Database column play_points SqlType(varchar), Length(128,true)
    *  @param cardsTypeForNext Database column cards_type_for_next SqlType(varchar), Length(32,true)
    *  @param keysForNext Database column keys_for_next SqlType(varchar), Length(64,true)
    *  @param gmtCreate Database column gmt_create SqlType(timestamp) */
  case class TmPlayRecordRow(id: Long, playType: Short, gameId: Long, memberId: Long, seqInGame: Short, cardsType: String, keys: String, handPoints: String, playPoints: String, cardsTypeForNext: String, keysForNext: String, gmtCreate: java.sql.Timestamp)
  /** GetResult implicit for fetching TmPlayRecordRow objects using plain SQL queries */
  implicit def GetResultTmPlayRecordRow(implicit e0: GR[Long], e1: GR[Short], e2: GR[String], e3: GR[java.sql.Timestamp]): GR[TmPlayRecordRow] = GR{
    prs => import prs._
      TmPlayRecordRow.tupled((<<[Long], <<[Short], <<[Long], <<[Long], <<[Short], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_play_record. Objects of this class serve as prototypes for rows in queries. */
  class TmPlayRecord(_tableTag: Tag) extends profile.api.Table[TmPlayRecordRow](_tableTag, "tm_play_record") {
    def * = (id, playType, gameId, memberId, seqInGame, cardsType, keys, handPoints, playPoints, cardsTypeForNext, keysForNext, gmtCreate) <> (TmPlayRecordRow.tupled, TmPlayRecordRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(playType), Rep.Some(gameId), Rep.Some(memberId), Rep.Some(seqInGame), Rep.Some(cardsType), Rep.Some(keys), Rep.Some(handPoints), Rep.Some(playPoints), Rep.Some(cardsTypeForNext), Rep.Some(keysForNext), Rep.Some(gmtCreate)).shaped.<>({r=>import r._; _1.map(_=> TmPlayRecordRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column play_type SqlType(int2) */
    val playType: Rep[Short] = column[Short]("play_type")
    /** Database column game_id SqlType(int8) */
    val gameId: Rep[Long] = column[Long]("game_id")
    /** Database column member_id SqlType(int8) */
    val memberId: Rep[Long] = column[Long]("member_id")
    /** Database column seq_in_game SqlType(int2) */
    val seqInGame: Rep[Short] = column[Short]("seq_in_game")
    /** Database column cards_type SqlType(varchar), Length(32,true) */
    val cardsType: Rep[String] = column[String]("cards_type", O.Length(32,varying=true))
    /** Database column keys SqlType(varchar), Length(64,true) */
    val keys: Rep[String] = column[String]("keys", O.Length(64,varying=true))
    /** Database column hand_points SqlType(varchar), Length(128,true) */
    val handPoints: Rep[String] = column[String]("hand_points", O.Length(128,varying=true))
    /** Database column play_points SqlType(varchar), Length(128,true) */
    val playPoints: Rep[String] = column[String]("play_points", O.Length(128,varying=true))
    /** Database column cards_type_for_next SqlType(varchar), Length(32,true) */
    val cardsTypeForNext: Rep[String] = column[String]("cards_type_for_next", O.Length(32,varying=true))
    /** Database column keys_for_next SqlType(varchar), Length(64,true) */
    val keysForNext: Rep[String] = column[String]("keys_for_next", O.Length(64,varying=true))
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
  }
  /** Collection-like TableQuery object for table TmPlayRecord */
  lazy val TmPlayRecord = new TableQuery(tag => new TmPlayRecord(tag))

  /** Entity class storing rows of table TmRoom
    *  @param id Database column id SqlType(int8), PrimaryKey
    *  @param name Database column name SqlType(varchar), Length(64,true), Default()
    *  @param status Database column status SqlType(int2)
    *  @param minTableId Database column min_table_id SqlType(int8), Default(0)
    *  @param maxTableId Database column max_table_id SqlType(int8), Default(0)
    *  @param seatCapacity Database column seat_capacity SqlType(int8), Default(0)
    *  @param currentPlayers Database column current_players SqlType(int8), Default(0)
    *  @param gmtCreate Database column gmt_create SqlType(timestamp)
    *  @param gmtUpdate Database column gmt_update SqlType(timestamp) */
  case class TmRoomRow(id: Long, name: String = "", status: Short, minTableId: Long = 0L, maxTableId: Long = 0L, seatCapacity: Long = 0L, currentPlayers: Long = 0L, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)
  /** GetResult implicit for fetching TmRoomRow objects using plain SQL queries */
  implicit def GetResultTmRoomRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Short], e3: GR[java.sql.Timestamp]): GR[TmRoomRow] = GR{
    prs => import prs._
      TmRoomRow.tupled((<<[Long], <<[String], <<[Short], <<[Long], <<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_room. Objects of this class serve as prototypes for rows in queries. */
  class TmRoom(_tableTag: Tag) extends profile.api.Table[TmRoomRow](_tableTag, "tm_room") {
    def * = (id, name, status, minTableId, maxTableId, seatCapacity, currentPlayers, gmtCreate, gmtUpdate) <> (TmRoomRow.tupled, TmRoomRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status), Rep.Some(minTableId), Rep.Some(maxTableId), Rep.Some(seatCapacity), Rep.Some(currentPlayers), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({r=>import r._; _1.map(_=> TmRoomRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(64,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true), O.Default(""))
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
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

  /** Entity class storing rows of table TmRoomConfig
    *  @param gameType Database column game_type SqlType(int4)
    *  @param deviceType Database column device_type SqlType(int4)
    *  @param minDiamondAmount Database column min_diamond_amount SqlType(int4)
    *  @param maxDiamondAmount Database column max_diamond_amount SqlType(int4)
    *  @param roomRanges Database column room_ranges SqlType(varchar), Length(64,true), Default()
    *  @param gmtCreate Database column gmt_create SqlType(timestamp)
    *  @param gmtUpdate Database column gmt_update SqlType(timestamp)
    *  @param topic Database column topic SqlType(varchar), Length(128,true) */
  case class TmRoomConfigRow(gameType: Int, deviceType: Int, minDiamondAmount: Int, maxDiamondAmount: Int, roomRanges: String = "", gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp, topic: String)
  /** GetResult implicit for fetching TmRoomConfigRow objects using plain SQL queries */
  implicit def GetResultTmRoomConfigRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TmRoomConfigRow] = GR{
    prs => import prs._
      TmRoomConfigRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table tm_room_config. Objects of this class serve as prototypes for rows in queries. */
  class TmRoomConfig(_tableTag: Tag) extends profile.api.Table[TmRoomConfigRow](_tableTag, "tm_room_config") {
    def * = (gameType, deviceType, minDiamondAmount, maxDiamondAmount, roomRanges, gmtCreate, gmtUpdate, topic) <> (TmRoomConfigRow.tupled, TmRoomConfigRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(gameType), Rep.Some(deviceType), Rep.Some(minDiamondAmount), Rep.Some(maxDiamondAmount), Rep.Some(roomRanges), Rep.Some(gmtCreate), Rep.Some(gmtUpdate), Rep.Some(topic)).shaped.<>({r=>import r._; _1.map(_=> TmRoomConfigRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column game_type SqlType(int4) */
    val gameType: Rep[Int] = column[Int]("game_type")
    /** Database column device_type SqlType(int4) */
    val deviceType: Rep[Int] = column[Int]("device_type")
    /** Database column min_diamond_amount SqlType(int4) */
    val minDiamondAmount: Rep[Int] = column[Int]("min_diamond_amount")
    /** Database column max_diamond_amount SqlType(int4) */
    val maxDiamondAmount: Rep[Int] = column[Int]("max_diamond_amount")
    /** Database column room_ranges SqlType(varchar), Length(64,true), Default() */
    val roomRanges: Rep[String] = column[String]("room_ranges", O.Length(64,varying=true), O.Default(""))
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
    /** Database column topic SqlType(varchar), Length(128,true) */
    val topic: Rep[String] = column[String]("topic", O.Length(128,varying=true))

    /** Primary key of TmRoomConfig (database name tm_room_config_pkey) */
    val pk = primaryKey("tm_room_config_pkey", (gameType, deviceType))
  }
  /** Collection-like TableQuery object for table TmRoomConfig */
  lazy val TmRoomConfig = new TableQuery(tag => new TmRoomConfig(tag))

  /** Entity class storing rows of table TmSeat
    *  @param id Database column id SqlType(int8), PrimaryKey
    *  @param status Database column status SqlType(int2)
    *  @param memberId Database column member_id SqlType(int8), Default(0)
    *  @param cards Database column cards SqlType(varchar), Length(128,true)
    *  @param landlordCards Database column landlord_cards SqlType(varchar), Length(128,true)
    *  @param proCardsInfo Database column pro_cards_info SqlType(varchar), Length(128,true)
    *  @param fingerPrint Database column finger_print SqlType(varchar), Length(64,true)
    *  @param gameId Database column game_id SqlType(int8)
    *  @param gameType Database column game_type SqlType(int4)
    *  @param deviceType Database column device_type SqlType(int4)
    *  @param multiples Database column multiples SqlType(int4), Default(1)
    *  @param baseAmount Database column base_amount SqlType(int4)
    *  @param landlordPosition Database column landlord_position SqlType(varchar)
    *  @param previousNickname Database column previous_nickname SqlType(varchar), Length(64,true)
    *  @param previousCardsCount Database column previous_cards_count SqlType(int2)
    *  @param nextNickname Database column next_nickname SqlType(varchar), Length(64,true)
    *  @param nextCardsCount Database column next_cards_count SqlType(int2)
    *  @param playStatus Database column play_status SqlType(varchar), Length(32,true)
    *  @param seqInGame Database column seq_in_game SqlType(int2)
    *  @param landlord Database column landlord SqlType(bool), Default(false)
    *  @param socketId Database column socket_id SqlType(int8)
    *  @param gmtUpdate Database column gmt_update SqlType(timestamp) */
  case class TmSeatRow(id: Long, status: Short, memberId: Long = 0L, cards: String, landlordCards: String, proCardsInfo: String, fingerPrint: String, gameId: Long, gameType: Int, deviceType: Int, multiples: Int = 1, baseAmount: Int, landlordPosition: Char, previousNickname: String, previousCardsCount: Short, nextNickname: String, nextCardsCount: Short, playStatus: String, seqInGame: Short, landlord: Boolean = false, socketId: Long, gmtUpdate: java.sql.Timestamp)
  /** GetResult implicit for fetching TmSeatRow objects using plain SQL queries */
  implicit def GetResultTmSeatRow(implicit e0: GR[Long], e1: GR[Short], e2: GR[String], e3: GR[Int], e4: GR[Char], e5: GR[Boolean], e6: GR[java.sql.Timestamp]): GR[TmSeatRow] = GR{
    prs => import prs._
      TmSeatRow.tupled((<<[Long], <<[Short], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[Long], <<[Int], <<[Int], <<[Int], <<[Int], <<[Char], <<[String], <<[Short], <<[String], <<[Short], <<[String], <<[Short], <<[Boolean], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_seat. Objects of this class serve as prototypes for rows in queries. */
  class TmSeat(_tableTag: Tag) extends profile.api.Table[TmSeatRow](_tableTag, "tm_seat") {
    def * = (id, status, memberId, cards, landlordCards, proCardsInfo, fingerPrint, gameId, gameType, deviceType, multiples, baseAmount, landlordPosition, previousNickname, previousCardsCount, nextNickname, nextCardsCount, playStatus, seqInGame, landlord, socketId, gmtUpdate) <> (TmSeatRow.tupled, TmSeatRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(status), Rep.Some(memberId), Rep.Some(cards), Rep.Some(landlordCards), Rep.Some(proCardsInfo), Rep.Some(fingerPrint), Rep.Some(gameId), Rep.Some(gameType), Rep.Some(deviceType), Rep.Some(multiples), Rep.Some(baseAmount), Rep.Some(landlordPosition), Rep.Some(previousNickname), Rep.Some(previousCardsCount), Rep.Some(nextNickname), Rep.Some(nextCardsCount), Rep.Some(playStatus), Rep.Some(seqInGame), Rep.Some(landlord), Rep.Some(socketId), Rep.Some(gmtUpdate)).shaped.<>({r=>import r._; _1.map(_=> TmSeatRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get, _17.get, _18.get, _19.get, _20.get, _21.get, _22.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column member_id SqlType(int8), Default(0) */
    val memberId: Rep[Long] = column[Long]("member_id", O.Default(0L))
    /** Database column cards SqlType(varchar), Length(128,true) */
    val cards: Rep[String] = column[String]("cards", O.Length(128,varying=true))
    /** Database column landlord_cards SqlType(varchar), Length(128,true) */
    val landlordCards: Rep[String] = column[String]("landlord_cards", O.Length(128,varying=true))
    /** Database column pro_cards_info SqlType(varchar), Length(128,true) */
    val proCardsInfo: Rep[String] = column[String]("pro_cards_info", O.Length(128,varying=true))
    /** Database column finger_print SqlType(varchar), Length(64,true) */
    val fingerPrint: Rep[String] = column[String]("finger_print", O.Length(64,varying=true))
    /** Database column game_id SqlType(int8) */
    val gameId: Rep[Long] = column[Long]("game_id")
    /** Database column game_type SqlType(int4) */
    val gameType: Rep[Int] = column[Int]("game_type")
    /** Database column device_type SqlType(int4) */
    val deviceType: Rep[Int] = column[Int]("device_type")
    /** Database column multiples SqlType(int4), Default(1) */
    val multiples: Rep[Int] = column[Int]("multiples", O.Default(1))
    /** Database column base_amount SqlType(int4) */
    val baseAmount: Rep[Int] = column[Int]("base_amount")
    /** Database column landlord_position SqlType(varchar) */
    val landlordPosition: Rep[Char] = column[Char]("landlord_position")
    /** Database column previous_nickname SqlType(varchar), Length(64,true) */
    val previousNickname: Rep[String] = column[String]("previous_nickname", O.Length(64,varying=true))
    /** Database column previous_cards_count SqlType(int2) */
    val previousCardsCount: Rep[Short] = column[Short]("previous_cards_count")
    /** Database column next_nickname SqlType(varchar), Length(64,true) */
    val nextNickname: Rep[String] = column[String]("next_nickname", O.Length(64,varying=true))
    /** Database column next_cards_count SqlType(int2) */
    val nextCardsCount: Rep[Short] = column[Short]("next_cards_count")
    /** Database column play_status SqlType(varchar), Length(32,true) */
    val playStatus: Rep[String] = column[String]("play_status", O.Length(32,varying=true))
    /** Database column seq_in_game SqlType(int2) */
    val seqInGame: Rep[Short] = column[Short]("seq_in_game")
    /** Database column landlord SqlType(bool), Default(false) */
    val landlord: Rep[Boolean] = column[Boolean]("landlord", O.Default(false))
    /** Database column socket_id SqlType(int8) */
    val socketId: Rep[Long] = column[Long]("socket_id")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }
  /** Collection-like TableQuery object for table TmSeat */
  lazy val TmSeat = new TableQuery(tag => new TmSeat(tag))

  /** Entity class storing rows of table TmTable
    *  @param id Database column id SqlType(int8), PrimaryKey
    *  @param name Database column name SqlType(varchar), Length(64,true), Default()
    *  @param status Database column status SqlType(int2)
    *  @param seatCapacity Database column seat_capacity SqlType(int4), Default(0)
    *  @param minSeatId Database column min_seat_id SqlType(int8), Default(0)
    *  @param maxSeatId Database column max_seat_id SqlType(int8), Default(0)
    *  @param currentPlayers Database column current_players SqlType(int4)
    *  @param gmtCreate Database column gmt_create SqlType(timestamp)
    *  @param gmtUpdate Database column gmt_update SqlType(timestamp) */
  case class TmTableRow(id: Long, name: String = "", status: Short, seatCapacity: Int = 0, minSeatId: Long = 0L, maxSeatId: Long = 0L, currentPlayers: Int, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)
  /** GetResult implicit for fetching TmTableRow objects using plain SQL queries */
  implicit def GetResultTmTableRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Short], e3: GR[Int], e4: GR[java.sql.Timestamp]): GR[TmTableRow] = GR{
    prs => import prs._
      TmTableRow.tupled((<<[Long], <<[String], <<[Short], <<[Int], <<[Long], <<[Long], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table tm_table. Objects of this class serve as prototypes for rows in queries. */
  class TmTable(_tableTag: Tag) extends profile.api.Table[TmTableRow](_tableTag, "tm_table") {
    def * = (id, name, status, seatCapacity, minSeatId, maxSeatId, currentPlayers, gmtCreate, gmtUpdate) <> (TmTableRow.tupled, TmTableRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(status), Rep.Some(seatCapacity), Rep.Some(minSeatId), Rep.Some(maxSeatId), Rep.Some(currentPlayers), Rep.Some(gmtCreate), Rep.Some(gmtUpdate)).shaped.<>({r=>import r._; _1.map(_=> TmTableRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(64,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(64,varying=true), O.Default(""))
    /** Database column status SqlType(int2) */
    val status: Rep[Short] = column[Short]("status")
    /** Database column seat_capacity SqlType(int4), Default(0) */
    val seatCapacity: Rep[Int] = column[Int]("seat_capacity", O.Default(0))
    /** Database column min_seat_id SqlType(int8), Default(0) */
    val minSeatId: Rep[Long] = column[Long]("min_seat_id", O.Default(0L))
    /** Database column max_seat_id SqlType(int8), Default(0) */
    val maxSeatId: Rep[Long] = column[Long]("max_seat_id", O.Default(0L))
    /** Database column current_players SqlType(int4) */
    val currentPlayers: Rep[Int] = column[Int]("current_players")
    /** Database column gmt_create SqlType(timestamp) */
    val gmtCreate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_create")
    /** Database column gmt_update SqlType(timestamp) */
    val gmtUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("gmt_update")
  }
  /** Collection-like TableQuery object for table TmTable */
  lazy val TmTable = new TableQuery(tag => new TmTable(tag))
}
