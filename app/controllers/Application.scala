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
import utils.security.Secured

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class Application @Inject()(webJarAssets: WebJarAssets, variantColumnRepository: VariantColumnRepository,
                            tabFieldFilterRepository: TabFieldFilterRepository, jdbcRepository: JDBCRepository, system: ActorSystem) extends Controller with Secured {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getFields(sample_fake_id: Int) = withUser { user =>
    request =>
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

  def getVariantColumn: Action[AnyContent] = Action.async { request =>
    variantColumnRepository.getAll.map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Variant column list successfully"))
    }
  }

  def getTranscriptData(sampleFakeId: Int) = withUser { user =>
    request =>
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

  def getByTab = withUser { user =>
    request =>
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

  def getSamplesList = withUser { user =>
    _ => {
      Await.result(
        PrivilegeRepository.getAll(user).map {
          list =>
            val response = list.map(item => SampleMetadataModel(item._1, item._2))
            Ok(successResponse(Json.toJson(response), "list of available samples"))
        }, Duration.Inf)
    }
  }

  def count = withUser { user =>
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

  def saveUserFields() = withUser { user =>
    request =>
      request.body.asJson.map {
        json =>
          json.asOpt[List[UserSampleTabDTO]].map {
            elem =>
              UserSmpTabRepository.save(user.id, elem)
              Ok(successResponse(Json.toJson(""), "Fields saved"))
          }.getOrElse {
            BadRequest("Missing parameter")
          }
      }.getOrElse {
        BadRequest("Expecting Json data")
      }
  }

  def getUsersList = withAdmin { user =>
    request =>
      Await.result(
        UserRepository.getAll.map {
          list =>
            Ok(successResponse(Json.toJson(list), "list of users"))
        }, Duration.Inf)
  }
}