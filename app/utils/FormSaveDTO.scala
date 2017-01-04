package utils


case class NewFormModel
(
  value: String,
  name: String,
  userId: Int,
  fieldFk: Int
)

case class FormSaveDTO
(
  name: String,
  fields: List[NewFormModel]
)
