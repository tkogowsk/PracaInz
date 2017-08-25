package utils

import ai.x.play.json.Jsonx
import models._
import utils.dtos._

object JsonFormat {

  implicit lazy val transcriptModelFormat = Jsonx.formatCaseClass[TranscriptModel]
  implicit lazy val variantColumnModelFormat = Jsonx.formatCaseClass[VariantColumnModel]
  implicit lazy val fieldModelFormat = Jsonx.formatCaseClass[FieldModel]
  implicit lazy val filterModelFormat = Jsonx.formatCaseClass[FilterModel]
  implicit lazy val sampleMetadataModelFormat = Jsonx.formatCaseClass[SampleMetadataModel]
  implicit lazy val privilegeModelFormat = Jsonx.formatCaseClass[PrivilegeModel]
  implicit lazy val fieldDTOFormat = Jsonx.formatCaseClass[FieldDTO]
  implicit lazy val filterDTOFormat = Jsonx.formatCaseClass[FilterDTO]
  implicit lazy val authenticationDTOFormat = Jsonx.formatCaseClass[AuthenticationDTO]
  implicit lazy val selectedSampleDTOFormat = Jsonx.formatCaseClass[SelectedSampleDTO]
  implicit lazy val filteringCounterFormat = Jsonx.formatCaseClass[FilteringCounter]
  implicit lazy val FilteringCountersDTOFormat = Jsonx.formatCaseClass[FilteringCountersDTO]
  implicit lazy val filteringCountersDTOResponseFormat = Jsonx.formatCaseClass[FilteringCountersDTOResponse]
  implicit lazy val userFieldsSaveDTOFormat = Jsonx.formatCaseClass[UserSampleTabDTO]
  implicit lazy val columnSortingDTOFormat = Jsonx.formatCaseClass[ColumnSortingDTO]
  implicit lazy val columnSearchDTOFormat = Jsonx.formatCaseClass[ColumnSearchDTO]
  implicit lazy val filterWithPaginationDTOFormat = Jsonx.formatCaseClass[FilterWithPaginationDTO]
  implicit lazy val userSmpTabModelFormat = Jsonx.formatCaseClass[UserSmpTabModel]
  implicit lazy val tabFieldFilterDTOFormat = Jsonx.formatCaseClass[TabFieldFilterDTO]
  implicit lazy val tabFieldFilterModelFormat = Jsonx.formatCaseClassUseDefaults[TabFieldFilterModel]
  implicit lazy val tabModelFormat = Jsonx.formatCaseClassUseDefaults[TabModel]
  implicit lazy val dataCellDTOFormat = Jsonx.formatCaseClassUseDefaults[DataCellDTO]
  implicit lazy val dataRowDTOFormat = Jsonx.formatCaseClassUseDefaults[DataRowDTO]
  implicit lazy val UserModelFormat = Jsonx.formatCaseClass[UserModel]
  implicit lazy val UserVisibleVariantColumnFormat = Jsonx.formatCaseClass[UserVisibleVariantColumnModel]
  implicit lazy val VisibleColumnDTOFormat = Jsonx.formatCaseClass[VisibleColumnDTO]

}


