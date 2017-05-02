package controllers

import play.api.mvc._
import javax.inject.Inject

import models.{FieldsModel, FormsModel}
import utils.{Constants, FormEditDTO, FormSaveDTO}
import repository._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.json.Json._

import scala.concurrent.ExecutionContext.Implicits.global
import utils.JsonFormat._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class Application @Inject()(webJarAssets: WebJarAssets, fieldRepository: FieldRepository, formsRepository: FormsRepository,
                            variantColumnRepository: VariantColumnRepository, transcriptRepository :TranscriptRepository) extends Controller {

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

  def getFields: Action[AnyContent] = Action.async {
    fieldRepository.getAll.map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Fields list successfully"))
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
}

