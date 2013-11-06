package com.scalapenos.patterns

import akka.actor.Actor
import scala.util.{Failure, Success}
import com.scalapenos.patterns.AsyncJobsProcessing._

class JobActor extends Actor {
  import JobActor._
  import context._

  var stuff: Stuff = _


  override def preStart(): Unit = {
    super.preStart()

    initSomeStuff.onComplete {
      case Success(newStuff) => stuff = newStuff
      case Failure(e)        => // what to do?
    }
  }

  def receive = {
    case job: Job => stuff.processJob(job)
  }
}