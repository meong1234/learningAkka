package com.akkademy.db.core

import org.scalatest.FunSpecLike
import org.scalatest.Matchers
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import com.akkademy.db.api.SetRequest

class AkkademyDbSpec extends FunSpecLike with Matchers{
  
  implicit val system = ActorSystem()
  
  describe("akkademyDb") {
    
    describe("SetRequest") {
      
      it("should place key/value to map") {
        val actorRef = TestActorRef(new AkkademyDb)
        actorRef ! SetRequest("key", "value")
        val actorDb = actorRef.underlyingActor
        print(actorDb.map.get("key"))
        actorDb.map.get("key") should equal(Some("value"))
      }
    }
  }
  
}