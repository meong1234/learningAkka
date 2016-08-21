package com.akkademy.db.core

import akka.actor.ActorSystem
import akka.actor.Props

object Main extends App{
  val system = ActorSystem("akkademy")
  val ref = system.actorOf(Props[AkkademyDb], name = "akkademy-db")
  println(ref.path)
}