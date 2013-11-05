package com.scalapenos.myapp
package api

import akka.actor.ActorSystem


object Boot extends App {

  val system = ActorSystem("my-app-api")

  // start some actors
}
