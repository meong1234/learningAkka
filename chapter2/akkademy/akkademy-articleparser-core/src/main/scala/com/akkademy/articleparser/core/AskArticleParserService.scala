package com.akkademy.articleparser.core

import akka.util.Timeout
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.pattern.ask
import com.akkademy.articleparser.message._
import com.akkademy.db.api.{ GetRequest, SetRequest }

import scala.concurrent.Future
import akka.actor.Status

class AskArticleParser(cacheActorPath: String,
                    httpClientActorPath: String,
                    articleParserActorPath: String,
                    implicit val timeout: Timeout) extends Actor with ActorLogging {

  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  //used for future
  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {

    case ParseArticle(uri) =>
      val senderRef = sender()

      val cacheResult = cacheActor ? GetRequest(uri)
      val result = cacheResult.recoverWith {
        //if not requested before yet
        case _: Exception =>

          val fRawResult = httpClientActor ? uri

          fRawResult flatMap {
            case HttpResponse(rawArticle) =>
              articleParserActor ? ParseHtmlArticle(uri, rawArticle)
            case x =>
              Future.failed(new Exception("unknown response"))
          }
      }

      result onComplete {
        case scala.util.Success(x: String) =>
          log.debug("cached result")
          senderRef ! x

        case scala.util.Success(x: ArticleBody) =>
          cacheActor ! SetRequest(uri, x.body)
          senderRef ! x

        case scala.util.Failure(t) =>
          senderRef ! akka.actor.Status.Failure(t)

        case x => log.debug("unknown message")
      }

    case o => Status.Failure(new ClassNotFoundException)
  }

}