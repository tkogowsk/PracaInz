package models

case class UserSmpTabModel
(
  smpl_id: String,
  user_id: Int,
  tab_field_filter_tab_id: Int,
  tab_field_filter_field_id: Int,
  tab_field_filter_filter_id: Int,
  value: String
)
