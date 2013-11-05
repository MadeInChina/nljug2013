package com.scalapenos.patterns

import scala.util._
import akka.actor._

class MyBecomeActor2 extends Actor {
  import AsyncJobsProcessing._
  import JobActor._
  import context._

  override def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy

  private[this] def emptyVector = Vector.empty[(ActorRef, Any)]
  private[this] var messages = emptyVector

  self ! Initialize

  def receive: Receive = uninitialized

  def uninitialized: Receive = {
    case Initialize => {
      initSomeStuff.onComplete {
        case Success(lib) => {
          val child = actorOf(Props[MyChildLib], "the-one-child")
          watch(child)

          child ! InitializedLib(lib)

          messages.foreach{ case (snd, msg) => child.tell(msg, snd) }
          messages = emptyVector

          become(initialized(child))
        }

        case Failure(e)   => self ! InitializationFailure(e)
      }
    }

    case InitializationFailure(e) => throw e

    case message: Any => messages = messages :+ (sender, message)
  }

  def initialized(child:ActorRef): Receive = {
    case msg:Request => child forward msg

    case Terminated(child) => {
      // in case the child is stopped you need to switch back to uninitialized
      // or kill yourself
      become(uninitialized)
      self ! Initialize  // or schedule to self.
    }
  }

  case class InitializedLib(lib: Stuff)

  private class MyChildLib extends Actor {
    def receive: Receive = uninitialized

    def uninitialized:Receive = {
      case InitializedLib(lib) => context.become(initialized(lib))
    }

    def initialized(lib: Stuff): Receive = {
      case Request(value) => lib.processJob
      // you could even respond here since parent forwards.
      // in the case an exception happens the parent stops the actor.
    }
  }
}