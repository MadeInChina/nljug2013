package com.scalapenos.myapp
package api

import akka.actor.ActorSystem
import akka.io.IO

import spray.can.Http


object Main extends App {
  implicit val system = ActorSystem("my-app-api")

  val api = system.actorOf(ApiActor.props, ApiActor.name)

  IO(Http) ! Http.Bind(listener = api,
    interface = "0.0.0.0",
    port = 8000)
}
