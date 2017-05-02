package utils

import models.FormsModel

@Deprecated
case class FormEditDTO
(
  name: String,
  fields: List[FormsModel]
)