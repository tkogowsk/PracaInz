package utils.dtos

case class TabFieldFilterDTO
(
  tab_id: Int,
  field_id: Int,
  filter_id: Int,
  default_value: Option[String],
  tab_name: String,
  variant_column_id: Int,
  options: Option[String],
  relation: String,
  filterName: Option[String],
  is_global: Option[Boolean],
  value: Option[String],
  smpl_id: Option[String]
)
