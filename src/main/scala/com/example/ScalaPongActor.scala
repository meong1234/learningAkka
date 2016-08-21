package com.example

import akka.actor.Actor
import akka.actor.Status

class ScalaPongActor extends Actor{
  
  override def receive: Receive = {
    case "Ping" => sender() ! "Pong"
    case _ => sender() ! Status.Failure(new Exception("unknown message")) 
  }
  
}