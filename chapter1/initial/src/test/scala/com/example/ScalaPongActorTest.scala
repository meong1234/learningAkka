package com.example

import scala.concurrent.Await
import scala.concurrent.duration._

import org.scalatest.FunSpecLike
import org.scalatest.Matchers
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Future

class ScalaPongActorTest extends FunSpecLike with Matchers {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5.seconds)

  val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))

  def askPong(message: String): Future[String] = (pongActor ? message).mapTo[String]

  describe("Pong Actor") {
    it("should respond with pong") {
      val future = pongActor ? "Ping"
      val result = Await.result(future, 1.second)
      assert(result == "Pong")
    }

    it("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future, 1.second)
      }
    }
  }

  describe("Future Example") {
    import scala.concurrent.ExecutionContext.Implicits.global
    
    it("should print to console") {
      askPong("Ping")
        .onSuccess {
          case x: String => println("replied with : " + x)
        }
      Thread.sleep(100)
    }

    it("should print error") {
      askPong("CauseError")
        .onFailure({
          case e: Exception => println("got exception : "+e.getMessage) 
        })
      Thread.sleep(100)
    }
    
    it("should recover from error") {
      askPong("CauseError")
        .recoverWith({
          case e: Exception => askPong("Ping") 
        })
      Thread.sleep(100)
    }
  }
}