package models

case class PrivilegeModel
(
  user_id: Int,
  smpl_id: String,
  access_type: Option[String],
  region_id: Option[Int]
)