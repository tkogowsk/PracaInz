package models

case class TabFieldFilterModel
(
  tab_id: Int,
  field_id: Int,
  filter_id: Int,
  default_value: Option[List[String]]
)

