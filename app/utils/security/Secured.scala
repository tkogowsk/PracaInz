package utils.security

import controllers.routes
import models.UserModel
import play.api.mvc._
import repository.UserRepository

trait Secured {
  def username(request: RequestHeader) = request.session.get("username")

  def onUnauthorized(request: RequestHeader) = {
    Results.Redirect(routes.Application.index())
  }

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  def withUser(f: UserModel => Request[AnyContent] => Result) = withAuth { username =>
    implicit request =>
      UserRepository.getByName(username).map { user =>
        f(user)(request).withCookies(Cookie("authenticated", "true", Option(1800), httpOnly = false), Cookie("role", user.role, Option(1800), httpOnly = false))
      }.getOrElse(onUnauthorized(request))
  }

  def withAdmin(f: UserModel => Request[AnyContent] => Result) = withAuth { username =>
    implicit request =>
      UserRepository.getIfAdmin(username).map { user =>
        f(user)(request).withCookies(Cookie("authenticated", "true", Option(1800), httpOnly = false), Cookie("role", user.role, Option(1800), httpOnly = false))
      }.getOrElse(onUnauthorized(request))
  }

}
