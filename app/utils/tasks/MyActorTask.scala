package utils.tasks

import javax.inject.Inject

import akka.actor.{ActorSystem, Props}
import utils.actors.PasswordActor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class MyActorTask @Inject()(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {
  val myActor = actorSystem.actorOf(Props[PasswordActor], name = "passwordtask")

  actorSystem.scheduler.schedule(
    initialDelay = 0.microseconds,
    interval = 10.minutes,
    receiver = myActor,
    message = "test"
  )

}