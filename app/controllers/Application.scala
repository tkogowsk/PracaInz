package controllers

import play.api.mvc._
import javax.inject.Inject

import utils.Constants
import repository.{TranscriptRepository, UserFilterSelectionRepository}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.libs.json.Json._

import scala.concurrent.ExecutionContext.Implicits.global
import utils.JsonFormat._

class Application @Inject()(webJarAssets: WebJarAssets, transcriptRepository: TranscriptRepository,
                            userFilterSelectionRepository: UserFilterSelectionRepository)extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getTranscript() = Action.async{
    transcriptRepository.getAll().map { res =>
      Ok(successResponse(Json.toJson(res), "Getting Transcript list successfully"))
    }
  }
  def getUserFilter() = Action.async{
    userFilterSelectionRepository.getById(1).map { res =>
      Ok(successResponse(Json.toJson(res), "Getting User Filter list successfully"))
    }
  }

}