package com.akkademy.db.core

import akka.actor.Actor
import scala.collection.immutable.HashMap
import akka.actor.ActorLogging 
import akka.actor.Status

class AkkademyDb extends Actor with ActorLogging{
  
  import com.akkademy.db.api._
  
  var map = new HashMap[String, Object]
  
  override def receive = {
    case SetRequest(key, value) => {
      log.info("recieve SetRequest : key - {} value {}", key, value)
      map += (key -> value)
      sender() ! Status.Success
    }
    
    case GetRequest(key) => {
      log.info("recieve GetRequest : key - {}", key)
      val data: Option[Object] = map.get(key)
      data match {
        case Some(x) => sender() ! x
        case None => sender() !  Status.Failure(new KeyNotFound(key)) 
      }
    }
    
    case o => Status.Failure(new ClassNotFoundException)
  }
}