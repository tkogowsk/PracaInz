package utils

import ai.x.play.json.Jsonx
import models._
import play.api.libs.json.Json

object JsonFormat {

  implicit val transcriptModelFormat = Jsonx.formatCaseClass[TranscriptModel]

}


