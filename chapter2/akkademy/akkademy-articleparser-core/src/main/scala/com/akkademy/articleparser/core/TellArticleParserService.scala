package com.akkademy.articleparser.core

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Status
import akka.actor.ActorRef
import akka.util.Timeout
import akka.actor.Props
import akka.actor.Status.Failure
import java.util.concurrent.TimeoutException
import com.akkademy.articleparser.message._
import com.akkademy.db.api._


class TellArticleParserService(cacheActorPath: String,
                               httpClientActorPath: String,
                               articleParserActorPath: String,
                               implicit val timeout: Timeout) extends Actor with ActorLogging {

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  implicit val ec = context.dispatcher

  override def receive: Receive = {

    case ParseArticle(uri) =>
      val extraActor = buildExtraActor(sender(), uri)

      cacheActor.tell(GetRequest(uri), extraActor)

      context.system.scheduler.scheduleOnce(timeout.duration, extraActor, "timeout")

    case o => Status.Failure(new ClassNotFoundException)
  }

  private def buildExtraActor(senderRef: ActorRef, uri: String): ActorRef = {
    return context.actorOf(Props(new Actor{
      override def receive = {
        case "timeout" => //if we get timeout, then fail
          senderRef ! Failure(new TimeoutException("timeout"))
          context.stop(self)
          
        case KeyNotFound(key) => //If we get not found key not found request to client
          httpClientActor ! key
          
        case HttpResponse(body) =>//If we get the http response first, we pass it to be parsed.
          articleParserActor ! ParseHtmlArticle(uri, body)
          
        case body: String => //If we get the cache response first, then we handle it and shut down.
           //The cache response will come back before the HTTP response so we never parse in this case.
          senderRef ! body
          context.stop(self)
          
        case ArticleBody(uri, body) =>//If we get the parsed article back, then we've just parsed it
          cacheActor ! SetRequest(uri, body)
          senderRef ! body
          
        case t => //We can get a cache miss
          println("ignoring msg: " + t.getClass)
          
      }
    }))
  }

}