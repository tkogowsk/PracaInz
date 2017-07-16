package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import models.SampleMetadataModel
import play.api.libs.json.Json._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repository._
import utils.JsonFormat._
import utils._
import utils.dtos._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class Application @Inject()(webJarAssets: WebJarAssets, variantColumnRepository: VariantColumnRepository,
                            userRepository: UserRepository, privilegeRepository: PrivilegeRepository,
                            tabFieldFilterRepository: TabFieldFilterRepository, jdbcRepository: JDBCRepository, system: ActorSystem) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getFields(sample_fake_id: Int, userName: String) = Action { request =>
    var userId = None: Option[Int]
    Await.result({
      userRepository.getByName(userName).map {
        user =>
          if (user.isDefined) {
            userId = Some(user.get.id)
          } else {
            BadRequest("User not found")
          }
      }
    }, Duration.Inf)

    Await.result(
      SampleMetadataRepository.getByFakeId(sample_fake_id).map {
        sample =>
          if (sample.isDefined) {
            Await.result(
              tabFieldFilterRepository.getFilter(sample.get.sample_id, 1).map { res =>
                val list = res.map(item => TabFieldFilterDTO(item._1, item._2, item._3, item._4, item._5, item._6, item._7, item._8, item._9, item._10, item._11, item._12))
                Ok(successResponse(Json.toJson(list), "Getting Variant column list successfully"))
              }, Duration.Inf)
          } else {
            BadRequest("Not found sample id")
          }
      }, Duration.Inf)
  }

  def getVariantColumn: Action[AnyContent] = Action.async {
    variantColumnRepository.getAll.map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Variant column list successfully"))
    }
  }

  def getTranscriptData(sampleFakeId: Int, userName: String): Action[AnyContent] = Action { request =>
    Await.result(
      SampleMetadataRepository.getByFakeId(sampleFakeId).map {
        sample =>
          if (sample.isDefined) {
            val response = jdbcRepository.getBySampleId(sample.get.sample_id)
            Ok(successResponse(Json.toJson(response), "list of  transcripts"))
          } else {
            BadRequest("Not found sample id")
          }
      }, Duration.Inf)
  }

  def getByTab = Action { request =>
    var sampleId = None: Option[String]
    request.body.asJson.map { json =>
      json.asOpt[FilterDTO].map { elem =>
        Await.result(
          SampleMetadataRepository.getByFakeId(elem.sampleFakeId).map {
            sample =>
              if (sample.isDefined) {
                sampleId = Some(sample.get.sample_id)
              } else {
                BadRequest("Sample not found")
              }
          }, Duration.Inf)

        val res = jdbcRepository.getByFields(elem, sampleId.get)
        Ok(successResponse(Json.toJson(res), "data from jdbc fetched"))
      }.getOrElse {
        BadRequest("Missing parameter")
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }

  }

  def logIn = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[AuthenticationDTO].map {
            elem =>
              Await.result(
                userRepository.authenticate(elem).map {
                  res =>
                    Ok(successResponse(Json.toJson(res.isDefined), ""))
                }, Duration.Inf)

          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def getSamplesList = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[AuthenticationDTO].map {
            elem =>
              Await.result(
                userRepository.authenticate(elem).map {
                  user =>
                    if (user.isDefined) {
                      Await.result(
                        privilegeRepository.getAll(user.orNull).map {
                          list =>
                            val response = list.map(item => SampleMetadataModel(item._1, item._2))
                            Ok(successResponse(Json.toJson(response), "list of available samples"))
                        }, Duration.Inf)
                    } else {
                      Ok(successResponse(Json.toJson("null"), "no data"))
                    }
                }, Duration.Inf)

          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def count = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[FilteringCountersDTO].map {
            elem =>
              val res = jdbcRepository.count(elem)
              Ok(successResponse(Json.toJson(res), ""))
          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def saveUserFields(userName: String) = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[List[UserSampleTabDTO]].map {
            elem =>
              var userId = None: Option[Int]
              Await.result({
                userRepository.getByName(userName).map {
                  user =>
                    if (user.isDefined) {
                      UserSmpTabRepository.save(user.get.id, elem)
                    } else {
                      BadRequest("User not found")
                    }
                }
              }, Duration.Inf)
              Ok(successResponse(Json.toJson(""), "Fields saved"))
          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def getTranscriptDataWithPagination(sampleFakeId: Int, offset: Int): Action[AnyContent] = Action { request =>
      request.body.asJson.map {
       json =>
         json.asOpt[FilterWithPaginationDTO].map {
            elem =>
              Await.result(
                SampleMetadataRepository.getByFakeId(sampleFakeId).map {
                  sample =>
                    if (sample.isDefined) {
                      val response = jdbcRepository.getBySampleId(sample.get.sample_id)
                      Ok(successResponse(Json.toJson(response), "list of  transcripts"))
                    } else {
                      BadRequest("Not found sample id")
                    }
                }, Duration.Inf)
              Ok(successResponse(Json.toJson(""), "Fields saved"))
          }.getOrElse {
            BadRequest("Missing parameter")
         }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

}