package com.scalapenos.myapp.api

import akka.actor._

object ApiActor {
  def props = Props[ApiActor]
  def name = "api"
}

class ApiActor extends Actor {

  def receive = {
    case msg =>
  }
}
