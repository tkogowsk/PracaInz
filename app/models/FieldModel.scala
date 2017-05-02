package models

case class FieldModel
(
  id: Int,
  variant_column_id: Int,
  options: Option[List[String]],
  relation: String
)

