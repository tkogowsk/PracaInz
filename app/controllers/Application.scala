package controllers

import play.api._
import play.api.mvc._
import slick.driver.PostgresDriver.api._
import javax.inject.Inject

import utils.Constants
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import repository.TranscriptRepository
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.libs.json.Json._
import scala.concurrent.ExecutionContext.Implicits.global
import utils.JsonFormat._

class Application @Inject()(webJarAssets: WebJarAssets, transcriptRepository: TranscriptRepository)extends Controller {

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

}