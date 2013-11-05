package com.scalapenos.myapp.api

import scala.concurrent.duration._

import akka.actor._
import akka.util.Timeout

import spray.routing._
import spray.http.StatusCodes._

object ApiActor {
  def props = Props[ApiActor]
  def name = "api"
}

class ApiActor extends Actor
                  with HttpService {

  val child = context.actorOf(Props[ProcessJob], "job")

  val executionContext = context.dispatcher
  val timeout = Timeout(15 seconds)

  def receive = runRoute(routes)

  def routes = {
    pathPrefix("api") {
      path("process") {
        post {
          entity(as[Job]) { job =>
            import akka.pattern.ask
            val result = processJob.ask(job).mapTo[JobResult]
            complete(OK, result)
          }
        }
      }
    }
  }

}

class ProcessJob extends Actor {
  def receive = {
    case msg => msg
  }
}

