package utils

import models.FormsModel

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
