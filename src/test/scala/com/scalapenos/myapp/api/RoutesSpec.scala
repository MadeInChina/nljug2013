package com.scalapenos.myapp.api

import org.specs2.mutable.Specification

import akka.actor.{Actor, ActorRefFactory, ActorRef, Props}

import spray.http.StatusCodes._
import spray.routing.HttpService

import spray.testkit.Specs2RouteTest

trait TestCreationSupport extends ActorCreationSupport
                             with HttpService {
  def createChild(props: Props, name: String): ActorRef = {
    actorRefFactory.actorOf(Props[FakeProcessJobActor], name)
  }
}

class RoutesSpec extends Specification
                    with Specs2RouteTest {
  val subject = new Routes with TestCreationSupport {
    implicit def actorRefFactory: ActorRefFactory = system
  }

  "The Job Routes" should {
    "process a job" in {
      Post("/api/jobs") ~> subject.routes ~> check {
        status === OK
        val result = responseAs[JobResult]
        result.value must beEqualTo("expected result")
      }
    }
  }
}

class FakeProcessJobActor extends Actor {
  def receive = {
    case Job(name) => sender ! JobResult("expected result")
  }
}
