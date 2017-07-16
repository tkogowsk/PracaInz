package controllers

import javax.inject.Inject

import play.api.libs.json.Json.obj
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, Cookie, DiscardingCookie}
import repository.UserRepository
import utils.Constants
import utils.JsonFormat._
import utils.dtos.AuthenticationDTO

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class Authorization @Inject()() extends Controller {

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def logIn = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[AuthenticationDTO].map {
            elem =>
              Await.result(
                UserRepository.authenticate(elem).map {
                  res =>
                    if (res.isDefined) {
                      Ok(successResponse(Json.toJson(true), ""))
                        .withSession(request.session + ("username" -> elem.name))
                        .withCookies(Cookie("authenticated", "true", Option(1800), httpOnly = false))
                    } else {
                      Unauthorized("Unauthorized")
                    }
                }, Duration.Inf)

          }.getOrElse {
            Unauthorized("Unauthorized")
          }
      }.getOrElse {
        Unauthorized("Unauthorized")
      }
  }

  def logOut = Action {
    Redirect(routes.Application.index()).withNewSession.discardingCookies(DiscardingCookie("authenticated")).flashing(
      "success" -> "You've been logged out"
    )
  }

}
