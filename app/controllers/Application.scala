package controllers

import javax.inject.Inject

import models.VariantColumnModel
import play.api.libs.json.Json._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repository._
import utils.JsonFormat._
import utils.{Constants, FilterDTO, TabFieldFilterDTO}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class Application @Inject()(webJarAssets: WebJarAssets, variantColumnRepository: VariantColumnRepository,
                            transcriptRepository: TranscriptRepository,
                            tabFieldFilterRepository: TabFieldFilterRepository, jdbcRepository:JDBCRepository) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getFields = Action.async {
    tabFieldFilterRepository.getFilter.map{ res =>
      val list = res.map(item => TabFieldFilterDTO(item._1, item._2,item._3,item._4,item._5, item._6, item._7, item._8, item._9, item._10, item._11, item._12))
      Ok(successResponse(Json.toJson(list), "Getting Variant column list successfully"))
    }
  }

  def getVariantColumn: Action[AnyContent] = Action.async {
    variantColumnRepository.getAll.map{ res =>
      Ok(successResponse(Json.toJson(res), "Getting Variant column list successfully"))
    }
  }

  def getAllJDBC: Action[AnyContent] = Action { response =>
    var columns: List[VariantColumnModel] = List[VariantColumnModel]()
    Await.result(
      variantColumnRepository.getAll.map {
        res =>
          columns = res
      }, Duration.Inf)

    val res = jdbcRepository.getAll(columns)
    Ok(successResponse(Json.toJson(res), "data from jdbc fetched"))
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

  /*
  def editForm = Action { request =>
    request.body.asJson.map { json =>
      json.asOpt[FormEditDTO].map { elem =>
        formsRepository.edit(elem.fields)
        Ok("Success")
      }.getOrElse {
        BadRequest("Missing parameter")
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }

  def saveForm = Action { request =>
    request.body.asJson.map { json =>
      json.asOpt[FormSaveDTO].map { elem =>
        formsRepository.save(elem.fields)
        Ok("Success")
      }.getOrElse {
        BadRequest("Missing parameter")
      }
    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }
  */
}

