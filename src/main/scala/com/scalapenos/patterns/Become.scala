package com.scalapenos.patterns

import scala.concurrent.Future
import scala.util._

import akka.actor._
import com.scalapenos.myapp.api.JobResult
import AsyncJobsProcessing._


object JobActor {
  def props = Props[JobActor]
  def name = "job"

  case object Initialize
  case class InitializationFailure(e:Throwable)
  case class InitializationException(message:String, e:Throwable) extends Exception(message, e)

  case class Job(name:String)
}



class JobActor extends Actor {
  import JobActor._

  import context._

  self ! Initialize

  def receive: Receive = uninitialized

  def uninitialized: Receive = {

    case Initialize => {
      initSomeStuff.onComplete {
        case Success(stuff) => become(initialized(stuff))
        case Failure(e)     => self ! InitializationFailure(e)
      }
    }
    case InitializationFailure(e) => throw e
    case _ => // ignore
  }

  def initialized(stuff: Stuff): Receive = {
    case job: Job => stuff.processJob(job)
  }
}


object AsyncJobsProcessing {
  import JobActor.Job
  trait Stuff {
    def processJob(job: Job):Future[JobResult]
  }

  def initSomeStuff: Future[Stuff] = Future.successful(new Stuff { def processJob(job:Job) = Future.successful(JobResult(""))})
}


