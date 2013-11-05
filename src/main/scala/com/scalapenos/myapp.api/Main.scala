package com.scalapenos.myapp.api

import akka.io.IO
import spray.can.Http
import akka.actor.ActorSystem

object Main {
  implicit val system = ActorSystem("my-app-api")

  val api = system.actorOf(ApiActor.props, ApiActor.name)

  IO(Http) ! Http.Bind(listener = api,
    interface = "0.0.0.0",
    port = 8000)
}