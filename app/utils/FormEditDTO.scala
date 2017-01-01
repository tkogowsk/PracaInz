package utils

import models.FormsModel

case class FormEditDTO
(
  name: String,
  fields: List[FormsModel]
)