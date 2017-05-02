package utils


@Deprecated
case class NewFormModel
(
  value: String,
  name: String,
  userId: Int,
  fieldFk: Int
)

@Deprecated
case class FormSaveDTO
(
  name: String,
  fields: List[NewFormModel]
)
