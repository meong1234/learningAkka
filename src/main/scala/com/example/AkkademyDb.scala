package com.example

import akka.actor.Actor
import akka.event.Logging
import scala.collection.immutable.HashMap
import scala.util.control.Exception
import akka.actor.Status
import com.example.messages._

class AkkademyDb extends Actor {
  var map = new HashMap[String, Object]
  val log = Logging(context.system, this)
  
  override def receive = {
    case SetRequest(key, value) => {
      log.info("recieved SetRequest - key: {} value {}", key, value)
      map = map + (key -> value)
      sender() ! Status.Success
    }
    case GetRequest(key) => {
      log.info("recieved GetRequest - key: {}", key)
      val response: Option[Object] = map.get(key)
      response match {
        case Some(x) => sender() ! x
        case None => sender() !  Status.Failure(new KeyNotFound(key))
      }
    }
    case o => Status.Failure(new ClassNotFoundException)
  }
}