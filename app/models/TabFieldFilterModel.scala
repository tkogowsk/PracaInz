package models

case class TabFieldFilterModel
(
  tabId: Int,
  fieldId: Int,
  filterId: Int,
  defaultValue: Option[String]
)

