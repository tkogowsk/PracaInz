package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.Json.obj
import utils.JsonFormat._
import play.api.mvc.Controller
import repository._
import utils.Constants
import utils.security.Secured

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class Admin @Inject()(webJarAssets: WebJarAssets, variantColumnRepository: VariantColumnRepository,
                            tabFieldFilterRepository: TabFieldFilterRepository, jdbcRepository: JDBCRepository, system: ActorSystem) extends Controller with Secured {

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getUsersList = withAdmin { user =>
    request =>
      Await.result(
        UserRepository.getAll.map {
          list =>
            Ok(successResponse(Json.toJson(list), "list of users"))
        }, Duration.Inf)
  }

  def getSamplesIds(userId: Int) = withAdmin { user =>
    request =>
      val list = jdbcRepository.getAllIds
      Ok(successResponse(Json.toJson(list), "list of samples"))
  }

  def getSamplesIdsList() = withAdmin { user =>
    request =>
      val list = jdbcRepository.getAllIds
      Ok(successResponse(Json.toJson(list), "list of samples"))
  }


  def getUserPrivilegesList(userId: Int) = withAdmin { user =>
    request =>
      Await.result(
        PrivilegeRepository.getAll(userId).map {
          list =>
            Ok(successResponse(Json.toJson(list), "list of available samples"))
        }, Duration.Inf)
  }

  def setUserPrivilegesList(userId: Int) = withAdmin { user =>
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[List[String]].map {
            elem =>
              PrivilegeRepository.save(userId, elem)
              Ok("success")
          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }
}
