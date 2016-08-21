package com.akkademy.articleparser.core

import akka.actor.Actor
import com.akkademy.articleparser.message._

class ParsingActor extends Actor{
  override def receive: Receive = {
    case ParseHtmlArticle(key, html) =>
      sender() ! ArticleBody(key, "html")
    case x =>
      println("unknown message " + x.getClass)
  }
}