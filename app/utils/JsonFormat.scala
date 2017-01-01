package utils

import ai.x.play.json.Jsonx
import models._
object JsonFormat {

  implicit val transcriptModelFormat = Jsonx.formatCaseClass[TranscriptModel]
  implicit val fieldsModelFormat = Jsonx.formatCaseClass[FieldsModel]
  implicit val formsModelFormat = Jsonx.formatCaseClass[FormsModel]
  implicit val newFormsModelFormat = Jsonx.formatCaseClass[NewFormModel]
  implicit lazy val formSaveDTOFormat = Jsonx.formatCaseClassUseDefaults[FormSaveDTO]
  implicit lazy val formEditDTOFormat = Jsonx.formatCaseClassUseDefaults[FormEditDTO]

}


