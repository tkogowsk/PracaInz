package controllers

import javax.inject.Inject

import models.{SampleMetadataModel, VariantColumnModel}
import play.api.libs.json.Json._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repository._
import utils.JsonFormat._
import utils._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class Application @Inject()(webJarAssets: WebJarAssets, variantColumnRepository: VariantColumnRepository,
                            userRepository: UserRepository, privilegeRepository: PrivilegeRepository,
                            tabFieldFilterRepository: TabFieldFilterRepository, jdbcRepository: JDBCRepository) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getFields = Action.async {
    tabFieldFilterRepository.getFilter.map { res =>
      val list = res.map(item => TabFieldFilterDTO(item._1, item._2, item._3, item._4, item._5, item._6, item._7, item._8, item._9, item._10, item._11, item._12))
      Ok(successResponse(Json.toJson(list), "Getting Variant column list successfully"))
    }
  }

  def getVariantColumn: Action[AnyContent] = Action.async {
    variantColumnRepository.getAll.map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Variant column list successfully"))
    }
  }

  def getTranscriptData(sample_fake_id: Int, userName: String): Action[AnyContent] = Action { request =>
    var columns: List[VariantColumnModel] = List[VariantColumnModel]()
    Await.result(
      variantColumnRepository.getAll.map {
        res =>
          columns = res
      }, Duration.Inf)

    Await.result(
      SampleMetadataRepository.getByFakeId(sample_fake_id).map {
        sample =>
          if (sample.isDefined) {
            val response = jdbcRepository.getBySampleId(columns, sample.get.sample_id)
            Ok(successResponse(Json.toJson(response), "list of  transcripts"))
          } else {
            BadRequest("Not found sample id")
          }
      }, Duration.Inf)
  }


  def getByTab = Action { request =>
    var columns: List[VariantColumnModel] = List[VariantColumnModel]()
    request.body.asJson.map { json =>
      json.asOpt[FilterDTO].map { elem =>
        Await.result(
          variantColumnRepository.getAll.map {
            res =>
              columns = res
          }, Duration.Inf)

        val res = jdbcRepository.getByFields(columns, elem)
        Ok(successResponse(Json.toJson(res), "data from jdbc fetched"))

      }.getOrElse {
        BadRequest("Missing parameter")
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }

  }

  def logIn = Action { request =>
    request.body.asJson.map { json =>
      json.asOpt[AuthenticationDTO].map { elem =>
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

  def getSamplesList = Action { request =>
    request.body.asJson.map { json =>
      json.asOpt[AuthenticationDTO].map { elem =>
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

}

