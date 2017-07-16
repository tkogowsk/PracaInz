package utils.tasks

import javax.inject.Inject

import akka.actor.{ActorSystem, Props}
import utils.actors.TokenActor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class MyActorTask @Inject()(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {
  val tokenActor = actorSystem.actorOf(Props[TokenActor], name = "passwordtask")

  actorSystem.scheduler.schedule(
    initialDelay = 0.microseconds,
    interval = 10.minutes,
    receiver = tokenActor,
    message = ""
  )

}