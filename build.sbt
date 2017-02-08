lazy val commonSettings = Seq(
  javacOptions ++= Seq("-encoding", "UTF-8"),
  organization := "com.jxjxgo.gamecenter",
  version := "1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "commons-codec" % "commons-codec" % "1.10",
    "net.codingwell" % "scala-guice_2.11" % "4.1.0",
    "org.scala-lang" % "scala-library" % "2.11.8",
    "com.jxjxgo.common" % "common-edecrypt_2.11" % "1.0",
    "com.google.protobuf" % "protobuf-java" % "3.0.0",
    "com.jxjxgo.common" % "common-utils_2.11" % "1.0",
    "com.jxjxgo.common" % "common-edecrypt_2.11" % "1.0",
    "com.jxjxgo.common" % "common-redis_2.11" % "1.0",
    "com.jxjxgo.sso" % "ssocommonlib_2.11" % "1.0"
  )
)

lazy val gamecommonlib = (project in file("gamecommonlib")).settings(commonSettings: _*).settings(
  name := """gamecommonlib""",
  libraryDependencies ++= Seq(
  )
)

lazy val gameserver = (project in file("gameserver")).settings(commonSettings: _*).settings(
  name := """gameserver""",
  libraryDependencies ++= Seq(
    "com.jxjxgo.common" % "common-db_2.11" % "1.0",
    "com.jxjxgo.common" % "common-error_2.11" % "1.0",
    "com.jxjxgo.gamecenter" % "gamecommonlib_2.11" % "1.0",
    "com.jxjxgo.member" % "membercommonlib_2.11" % "1.0"
  )
)
