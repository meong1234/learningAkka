package com.example

import akka.actor.ActorSystem
import akka.actor.Props

object Main extends App{
  val system = ActorSystem("akkademy")
  system.actorOf(Props[AkkademyDb], name = "akkademy-db")
}