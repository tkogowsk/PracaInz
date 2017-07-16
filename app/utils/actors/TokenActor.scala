package utils.actors

import akka.actor._
import repository.FieldRepository
import utils.MyPostgresDriver.api._

class TokenActor extends Actor {
  var field = FieldRepository.field
  private val db = Database.forConfig("postgresConf")

  def receive = {
    case "test" ⇒ {
      println("received test")
    }
    case _ ⇒ {
      println("received unknown message")
    }
  }
}
