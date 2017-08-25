package models

case class PrivilegeModel
(
  userId: Int,
  smplId: String,
  access_type: Option[String],
  region_id: Option[Int]
)