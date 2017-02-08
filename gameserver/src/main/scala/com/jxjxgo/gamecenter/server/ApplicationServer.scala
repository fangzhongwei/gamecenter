package com.jxjxgo.gamecenter.server

import java.util

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.jxjxgo.common.helper.ConfigHelper
import com.jxjxgo.common.rabbitmq.{RabbitmqConsumerTemplate, RabbitmqConsumerTemplateImpl}
import com.jxjxgo.common.redis.{RedisClientTemplate, RedisClientTemplateImpl}
import com.jxjxgo.gamecenter.service.{CoordinateService, CoordinateServiceImpl}
import com.jxjxgo.memberber.rpc.domain.MemberEndpoint
import com.jxjxgo.scrooge.thrift.template.{ScroogeThriftServerTemplate, ScroogeThriftServerTemplateImpl}
import com.twitter.finagle.Thrift
import com.twitter.util.Future
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

/**
  * Created by fangzhongwei on 2016/11/21.
  */
object ApplicationServer extends App {
  var logger = LoggerFactory.getLogger(this.getClass)

  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      val map: util.HashMap[String, String] = ConfigHelper.configMap
      Names.bindProperties(binder(), map)
      val config: Config = ConfigFactory.load()
      bind(classOf[RabbitmqConsumerTemplate]).to(classOf[RabbitmqConsumerTemplateImpl]).asEagerSingleton()
      bind(classOf[RedisClientTemplate]).to(classOf[RedisClientTemplateImpl]).asEagerSingleton()
      bind(new TypeLiteral[MemberEndpoint[Future]](){}).toInstance(Thrift.client.newIface[MemberEndpoint[Future]](config.getString("member.thrift.host.port")))
      bind(classOf[CoordinateService]).to(classOf[CoordinateServiceImpl]).asEagerSingleton()
      bind(classOf[ScroogeThriftServerTemplate]).to(classOf[ScroogeThriftServerTemplateImpl]).asEagerSingleton()
    }
  })

  private val consumerTemplate: RabbitmqConsumerTemplate = injector.getInstance(classOf[RabbitmqConsumerTemplate])
  consumerTemplate.connect
  consumerTemplate.startConsume("queue-game-wait-1000", injector.getInstance(classOf[CoordinateService]))
  consumerTemplate.startConsume("queue-game-wait-2000", injector.getInstance(classOf[CoordinateService]))
  consumerTemplate.startConsume("queue-game-wait-4000", injector.getInstance(classOf[CoordinateService]))
  consumerTemplate.startConsume("queue-game-wait-8000", injector.getInstance(classOf[CoordinateService]))
  consumerTemplate.startConsume("queue-game-wait-20000", injector.getInstance(classOf[CoordinateService]))
}
