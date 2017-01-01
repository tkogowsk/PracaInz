package controllers

import play.api.mvc._
import javax.inject.Inject

import models.{FieldsModel, FormsModel}
import utils.{Constants}
import repository.{FieldsRepository, FormsRepository, TranscriptRepository}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.libs.json.Json._

import scala.concurrent.ExecutionContext.Implicits.global
import utils.JsonFormat._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Application @Inject()(webJarAssets: WebJarAssets, transcriptRepository: TranscriptRepository,
                            fieldsRepository: FieldsRepository, formsRepository: FormsRepository
                            ) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getTranscript() = Action.async {
    transcriptRepository.getAll().map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Transcript list successfully"))
    }
  }

  def getByFilter(filterName: String, userId: Int) = Action.async {
    var fields: List[FieldsModel] = List[FieldsModel]()
    var userForm : List[FormsModel] = List[FormsModel]()

    Await.result(
      fieldsRepository.getAll().map {
        res =>
          fields = res
      }, Duration.Inf)

    Await.result(
      formsRepository.getUserForm(filterName, userId).map {
        res =>
          userForm = res
      }, Duration.Inf)

    transcriptRepository.getByFilter(fields, userForm).map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Transcript list successfully"))
    }
  }
}