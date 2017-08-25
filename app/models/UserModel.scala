package models

case class UserModel
(
  id: Int,
  name: String,
  role: String,
  salt: String,
  hash: String
)

