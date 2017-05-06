package utils

import ai.x.play.json.Jsonx
import models._
object JsonFormat {

  implicit val transcriptModelFormat = Jsonx.formatCaseClass[TranscriptModel]
  implicit val variantColumnModelFormat = Jsonx.formatCaseClass[VariantColumnModel]
  implicit val fieldModelFormat = Jsonx.formatCaseClass[FieldModel]
  implicit val filterModelFormat = Jsonx.formatCaseClass[FilterModel]
  implicit val fieldDTOFormat = Jsonx.formatCaseClass[FieldDTO]
  implicit val filterDTOFormat = Jsonx.formatCaseClass[FilterDTO]
  implicit val authenticationDTOFormat = Jsonx.formatCaseClass[AuthenticationDTO]

  implicit val userSmpTabModelFormat = Jsonx.formatCaseClass[UserSmpTabModel]
  implicit lazy val tabFieldFilterDTOFormat = Jsonx.formatCaseClass[TabFieldFilterDTO]
  implicit lazy val tabFieldFilterModelFormat = Jsonx.formatCaseClassUseDefaults[TabFieldFilterModel]
  implicit lazy val tabModelFormat = Jsonx.formatCaseClassUseDefaults[TabModel]
  implicit lazy val dataCellDTOFormat = Jsonx.formatCaseClassUseDefaults[DataCellDTO]
  implicit lazy val dataRowDTOFormat = Jsonx.formatCaseClassUseDefaults[DataRowDTO]


}


