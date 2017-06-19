package utils.dtos

case class UploadRowDTO
(
  tabName: String,
  filterName: String,
  fieldName: String,
  variantColumnName: String,
  relation: String,
  defaultValue: String,
  options: String
)
