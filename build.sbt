

lazy val commonSettings = Seq(
  javacOptions ++= Seq("-encoding", "UTF-8"),
  organization := "com.lawsofnature.gamecenter",
  version := "1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "commons-codec" % "commons-codec" % "1.10",
    "net.codingwell" % "scala-guice_2.11" % "4.1.0",
    "org.scala-lang" % "scala-library" % "2.11.8",
    "com.lawsofnature.common" % "common-edecrypt_2.11" % "1.0",
    "mysql" % "mysql-connector-java" % "5.1.36",
    "com.rabbitmq" % "amqp-client" % "3.6.5",
    "com.zeroc" % "ice" % "3.6.2",
    "com.typesafe.akka" %% "akka-actor" % "2.4.14",
    "com.typesafe.akka" %% "akka-cluster" % "2.4.14",
    "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4.14",
    "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.14",
    "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.14",
    "com.typesafe.akka" %% "akka-stream" % "2.4.14",
    "com.google.protobuf" % "protobuf-java" % "3.0.0",
    "com.lawsofnature.common" % "common-utils_2.11" % "1.0",
    "com.lawsofnature.common" % "common-edecrypt_2.11" % "1.0",
    "com.lawsofnature.common" % "common-redis_2.11" % "1.0",
    "com.lawsofnature.client" % "ssoclient_2.11" % "1.0",
    "com.lawsofnature.common" % "common-rabbitmq_2.12.0-RC2" % "1.0"
  )
)


lazy val gamecommonlib = (project in file("gamecommonlib")).settings(commonSettings: _*).settings(
  name := """gamecommonlib""",
  libraryDependencies ++= Seq(
    "com.lawsofnature.common" % "common-ice_2.11" % "1.0"
  )
)

lazy val gameserver = (project in file("gameserver")).settings(commonSettings: _*).settings(
  name := """gameserver""",
  libraryDependencies ++= Seq(
    "com.lawsofnature.common" % "common-ice_2.11" % "1.0",
    "com.lawsofnature.gamecenter" % "gamecommonlib_2.11" % "1.0"
  )
)
