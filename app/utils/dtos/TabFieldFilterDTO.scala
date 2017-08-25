package utils.dtos

case class TabFieldFilterDTO
(
  tabId: Int,
  fieldId: Int,
  filterId: Int,
  defaultValue: Option[String],
  tabName: String,
  variantColumnId: Int,
  options: Option[String],
  relation: String,
  filterName: Option[String],
  value: Option[String],
  smplId: Option[String]
)

