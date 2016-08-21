package com.akkademy.db.client

import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.db.api._

class SClient(remoteAddress: String) {
  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val remoteDb = system.actorSelection(s"akka.tcp://akkademy@$remoteAddress/user/akkademy-db")
  
  println(remoteDb.anchorPath)
  
  def set(key: String, value: Object) = {
    remoteDb ? SetRequest(key, value)
  }
  
  def get(key: String) = {
    remoteDb ? GetRequest(key)
  }
}