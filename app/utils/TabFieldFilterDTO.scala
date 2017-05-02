package utils

case class TabFieldFilterDTO
(
  tab_id: Int,
  field_id: Int,
  filter_id: Int,
  default_value: List[String],
  tab_name: String,
  is_global: Boolean,
  fieldName: String,
  variant_column_id: Int,
  options: List[String],
  relation: String
)
