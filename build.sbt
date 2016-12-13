name := "gamecenter"

version := "1.0"

scalaVersion := "2.11.8"
// https://mvnrepository.com/artifact/com.baidu/jprotobuf-rpc-core
libraryDependencies += "com.baidu" % "jprotobuf-rpc-core" % "3.4.2"
//libraryDependencies += "com.baidu" % "jprotobuf-rpc-core-spring" % "3.4.2"
//libraryDependencies += "com.baidu" % "jprotobuf-rpc-registry-redis" % "3.4.2"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.rabbitmq" % "amqp-client" % "3.6.5",
  "com.zeroc" % "ice" % "3.6.2",
  "com.typesafe.akka" %% "akka-actor" % "2.4.14",
  "com.typesafe.akka" %% "akka-stream" % "2.4.14"
)

// https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java
libraryDependencies += "com.google.protobuf" % "protobuf-java" % "3.0.0"


libraryDependencies += "com.lawsofnature.common" % "common-utils_2.11" % "1.0"
libraryDependencies += "com.lawsofnature.common" % "common-edecrypt_2.11" % "1.0"