package controllers

import javax.inject.Inject

import play.api.libs.json.Json.obj
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller, Cookie, DiscardingCookie}
import repository.UserRepository
import utils.Constants
import utils.JsonFormat._
import utils.dtos.AuthenticationDTO

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
              try {
                UserRepository.authenticate(elem).map {
                  res =>
                    Ok(successResponse(Json.toJson(true), ""))
                      .withSession(request.session + ("username" -> elem.name))
                      .withCookies(Cookie("authenticated", "true", Option(1800), httpOnly = false))
                }.getOrElse {
                  Unauthorized("Unauthorized")
                }
              }
              catch {
                case e: IllegalAccessException => {
                  Unauthorized("Unauthorized")
                }
              }
          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def logOut = Action {
    Redirect(routes.Application.index()).withNewSession.discardingCookies(DiscardingCookie("authenticated"), DiscardingCookie("role")).flashing(
      "success" -> "You've been logged out"
    )
  }

  def register = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[AuthenticationDTO].map {
            elem =>
              try {
                if (!isLoginNameFree(elem.name)) {
                  Unauthorized("Name not available")
                  throw new IllegalArgumentException
                }
                UserRepository.register(elem)
                Ok(successResponse(Json.toJson(true), "Success. You can log in now."))
              }
              catch {
                case e: IllegalArgumentException => {
                  Unauthorized("Login name not available. Please try different.")
                }
                case e: Exception => {
                  Unauthorized("Error during register. Please try later.")
                }
              }
          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }


  def isLoginNameFree(name: String): Boolean = {
    UserRepository.getByName(name).map {
      _ =>
        return false
    }
    true
  }

}
