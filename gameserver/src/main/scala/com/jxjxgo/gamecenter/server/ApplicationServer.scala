package com.jxjxgo.gamecenter.server

import java.util

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.jxjxgo.account.rpc.domain.AccountEndpoint
import com.jxjxgo.common.helper.ConfigHelper
import com.jxjxgo.common.kafka.template.{ConsumerTemplate, ConsumerTemplateImpl, ProducerTemplate, ProducerTemplateImpl}
import com.jxjxgo.common.redis.{RedisClientTemplate, RedisClientTemplateImpl}
import com.jxjxgo.gamecenter.repo.{TowVsOneRepository, TowVsOneRepositoryImpl}
import com.jxjxgo.gamecenter.service._
import com.jxjxgo.memberber.rpc.domain.MemberEndpoint
import com.jxjxgo.scrooge.thrift.template.{ScroogeThriftServerTemplate, ScroogeThriftServerTemplateImpl}
import com.twitter.finagle.Thrift
import com.twitter.scrooge.ThriftService
import com.twitter.util.Future
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by fangzhongwei on 2016/11/21.
  */
object ApplicationServer extends App {
  //  private[this] var logger = LoggerFactory.getLogger(this.getClass)

  override def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load()
    val injector = Guice.createInjector(new AbstractModule() {
      override def configure() {
        val map: util.HashMap[String, String] = ConfigHelper.configMap
        Names.bindProperties(binder(), map)
        bind(classOf[RedisClientTemplate]).to(classOf[RedisClientTemplateImpl]).asEagerSingleton()
        bind(classOf[ProducerTemplate]).to(classOf[ProducerTemplateImpl]).asEagerSingleton()
        bind(classOf[ConsumerTemplate]).to(classOf[ConsumerTemplateImpl]).asEagerSingleton()
        bind(classOf[TowVsOneRepository]).to(classOf[TowVsOneRepositoryImpl]).asEagerSingleton()
        bind(new TypeLiteral[MemberEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[MemberEndpoint[Future]](config.getString("member.thrift.host.port")))
        bind(new TypeLiteral[AccountEndpoint[Future]]() {}).toInstance(Thrift.client.newIface[AccountEndpoint[Future]](config.getString("account.thrift.host.port")))
        bind(classOf[CoordinateService]).to(classOf[CoordinateServiceImpl]).asEagerSingleton()
        bind(classOf[GameService]).to(classOf[GameServiceImpl]).asEagerSingleton()
        bind(classOf[ThriftService]).to(classOf[GameEndpointImpl]).asEagerSingleton()
        bind(classOf[ScroogeThriftServerTemplate]).to(classOf[ScroogeThriftServerTemplateImpl]).asEagerSingleton()
      }
    })

    injector.getInstance(classOf[ProducerTemplate]).init

    val consumerTemplate: ConsumerTemplate = injector.getInstance(classOf[ConsumerTemplate])
    consumerTemplate.init
    consumerTemplate.consume(config.getString("kafka.topic.game.join.T1010"))
    consumerTemplate.consume(config.getString("kafka.topic.game.join.T1020"))
    consumerTemplate.consume(config.getString("kafka.topic.game.join.T1050"))
    consumerTemplate.consume(config.getString("kafka.topic.game.join.T1100"))
    consumerTemplate.consume(config.getString("kafka.topic.game.join.T1200"))
    consumerTemplate.consume(config.getString("kafka.topic.game.join.T1500"))
  }
}
