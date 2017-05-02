package models

case class FilterModel
(
  id: Int,
  name: Option[String],
  is_global: Option[Boolean]
)

