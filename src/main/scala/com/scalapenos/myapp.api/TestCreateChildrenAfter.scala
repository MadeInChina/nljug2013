package com.scalapenos.myapp.api

import scala.concurrent.duration._

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask

import spray.routing._
import spray.http.StatusCodes._


object ApiActor {
  def props = Props[ApiActor]
  def name = "api"
}

class ApiActor extends HttpServiceActor
with Routes
with ActorContextCreationSupport
with ActorExecutionContextSupport {

  implicit val timeout = Timeout(15 seconds)

  def receive = runRoute(routes)

}


trait Routes extends HttpService
with ActorCreationSupport {

  val processJob = createChild(ProcessJob.props, ProcessJob.name)

  def routes = {
    pathPrefix("api") {
      path("jobs") {
        post {
          entity(as[Job]) { job =>
            val result = processJob.ask(job).mapTo[JobResult]
            complete(OK, result)
          }
        }
      }
    }
  }
}



trait ExecutionContextSupport {
  import scala.concurrent.ExecutionContext

  implicit def executionContext: ExecutionContext
}

trait ActorExecutionContextSupport extends ExecutionContextSupport { this: Actor =>
  implicit def executionContext = context.dispatcher
}



trait ActorCreationSupport {
  def createChild(props: Props, name: String): ActorRef
}

trait ActorContextCreationSupport extends ActorCreationSupport {
  def context: ActorContext
  def createChild(props: Props, name: String): ActorRef = context.actorOf(props, name)
}



object ProcessJob {
  def props = Props[ProcessJob]
  def name = "process-job"
}

class ProcessJob extends Actor {
  def receive = {
    case msg => msg
  }
}
