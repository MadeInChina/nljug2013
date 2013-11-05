package com.scalapenos.myapp.api

import akka.actor._
import spray.routing._
import spray.http.StatusCodes._

object ApiActor {
  def props = Props[ApiActor]
  def name = "api"
}

class ApiActor extends Actor
                  with HttpService {

  def receive = runRoute(routes)

  def routes = {
    pathPrefix("api") {
      path("process") {
        post {
          entity(as[Job]) { job =>
            val result = ...
            complete(OK, result)
          }
        }
      }
    }
  }

}