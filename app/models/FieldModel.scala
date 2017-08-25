package models

case class FieldModel
(
  id: Int,
  variantColumnId: Int,
  options: Option[String],
  relation: String
)

