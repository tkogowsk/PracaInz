package controllers

import play.api.mvc._
import javax.inject.Inject

import models.{FieldsModel, FormsModel}
import utils.{Constants, FormEditDTO, FormSaveDTO, TabFieldFilterDTO}
import repository._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.Json._

import scala.concurrent.ExecutionContext.Implicits.global
import utils.JsonFormat._


class Application @Inject()(webJarAssets: WebJarAssets, formsRepository: FormsRepository,
                            variantColumnRepository: VariantColumnRepository, transcriptRepository :TranscriptRepository,
                            tabFieldFilterRepository: TabFieldFilterRepository, jdbcRepository:JDBCRepository) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getTranscript: Action[AnyContent] = Action.async {
    transcriptRepository.getAll.map{ res =>
    Ok(successResponse(Json.toJson(res), "Getting Transcript list successfully"))
    }
  }


  def getUserForms(userId: Int): Action[AnyContent] = Action.async {
    formsRepository.getUserForms(userId).map { res =>
      Ok(successResponse(Json.toJson(res.groupBy(_.name)), "Getting User Forms list successfully"))
    }
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

/*  def getByFilter(filterName: String, userId: Int) = Action {
    var fields: List[FieldsModel] = List[FieldsModel]()
    var userForm: List[FormsModel] = List[FormsModel]()

    Await.result(
      fieldsRepository.getAll.map {
        res =>
          fields = res
      }, Duration.Inf)

    Await.result(
      formsRepository.getUserForm(filterName, userId).map {
        res =>
          userForm = res
      }, Duration.Inf)

    Ok(successResponse(Json.toJson("null"), "Getting Fields list successfully"))

  }*/

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

  def jdbcTest = Action{ response =>
    jdbcRepository.test
    Ok("jdbc test succeed")
  }
}

