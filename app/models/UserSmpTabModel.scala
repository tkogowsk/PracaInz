package models

case class UserSmpTabModel
(
  smplId: String,
  userId: Int,
  tabFieldFilterTabId: Int,
  tabFieldFilterFieldId: Int,
  tabFieldFilterFilterId: Int,
  value: String
)
