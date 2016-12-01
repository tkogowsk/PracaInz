package utils

import ai.x.play.json.Jsonx
import models._
object JsonFormat {

  implicit val transcriptModelFormat = Jsonx.formatCaseClass[TranscriptModel]
  implicit val userFilterSelectionModelFormat = Jsonx.formatCaseClass[UserFilterSelectionModel]

}


