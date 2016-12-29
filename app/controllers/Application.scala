package controllers

import play.api.mvc._
import javax.inject.Inject

import utils.{Constants, UserService}
import repository.{FieldsRepository, FormsRepository, TranscriptRepository}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.libs.json.Json._

import scala.concurrent.ExecutionContext.Implicits.global
import utils.JsonFormat._

class Application @Inject()(webJarAssets: WebJarAssets, transcriptRepository: TranscriptRepository,
                            fieldsRepository: FieldsRepository, formsRepository: FormsRepository,
                            userService : UserService) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }

  def angular(any: Any) = index

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }

  def getTranscript() = Action.async{
    transcriptRepository.getAll().map { res =>
/*
      println(res.groupBy(_.name))
*/
      Ok(successResponse(Json.toJson(res), "Getting Transcript list successfully"))
    }
  }

  def getByFilter(filterId: Int, userId: Int) = Action.async{
    transcriptRepository.getByFilterId(filterId, userId).map { res =>
      println(res)
      Ok(successResponse(Json.toJson(res), "Getting Transcript list successfully"))
    }
  }
}