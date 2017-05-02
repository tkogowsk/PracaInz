package utils

case class TabFieldFilterDTO
(
  tab_id: Int,
  field_id: Int,
  filter_id: Int,
  default_value: Option[List[String]],
  tab_name: String,
  variant_column_id: Int,
  options: Option[List[String]],
  relation: String,
  filterName: Option[String],
  is_global: Option[Boolean]
)

