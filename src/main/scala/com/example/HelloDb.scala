package com.example

import akka.actor.Actor
import akka.event.Logging
import scala.collection.immutable.HashMap


case class SetRequest(key: String, value: Object)

class HelloDb extends Actor {
  var map = new HashMap[String, Object]
  val log = Logging(context.system, this)
  
  override def receive = {
    case SetRequest(key, value) => {
      log.info("recieved SetRequest - key: {} value {}", key, value)
      map = map + (key -> value)
    }
    case o => log.info("recieved unknown message: {}", o)
  }

}