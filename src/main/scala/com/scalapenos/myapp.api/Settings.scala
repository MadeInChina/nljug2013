package com.scalapenos.myapp.api

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._

import akka.actor._
import com.typesafe.config.Config


class Settings(config: Config, extendedSystem: ExtendedActorSystem) extends Extension {

  object Http {
    val Port = config.getInt("my-app.http.port")
    val Host = config.getString("my-app.http.host")
  }

  val askTimeout = FiniteDuration(config.getMilliseconds("my-app.ask-timeout"), TimeUnit.MILLISECONDS)
}


object Settings extends ExtensionId[Settings] with ExtensionIdProvider {
  override def lookup = Settings
  override def createExtension(system: ExtendedActorSystem) = new Settings(system.settings.config, system)
  def apply(implicit context: ActorContext): Settings = apply(context.system)
}
