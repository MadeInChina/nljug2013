package com.scalapenos.patterns

import scala.concurrent.Future
import scala.util._

import akka.actor._


object MyActor {
  case object Init
  case class InitializationFailure(e:Throwable)
  case class InitializationException(message:String, e:Throwable) extends Exception(message, e)

  case class Request(value:String)
}

class MyActor extends Actor {
  import AsyncLib._
  import MyActor._

  import context._

  self ! Init

  def receive: Receive = uninitialized

  def uninitialized: Receive = {

    case Init => {
      initializeLib.onComplete {
        case Success(lib) => become(initialized(lib))
        case Failure(e)   => self ! InitializationFailure(e)
      }
    }
    case InitializationFailure(e)  => throw e
    case _ => // ignore or stash
  }

  def initialized(lib:Lib): Receive = {
    case msg:Request => lib.doSomeAsyncStuff
  }
}


object AsyncLib {
  trait Lib {
    def doSomeAsyncStuff:Future[Int]
  }

  def initializeLib: Future[Lib] = Future.successful(new Lib { def doSomeAsyncStuff = Future.successful(1)})
}


