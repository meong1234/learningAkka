import org.scalatest.FunSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterEach
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import com.example.HelloDb
import com.example.SetRequest

class HelloDbSpec extends FunSpecLike with Matchers
    with BeforeAndAfterEach {

  implicit val system = ActorSystem()

  describe("HelloDb") {
    describe("given SetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new HelloDb)
        actorRef ! SetRequest("1", "2")
        val helloDb = actorRef.underlyingActor
        print(helloDb.map.get("1"))
        helloDb.map.get("1") should equal(Some("2"))
      }
    }
  }

}