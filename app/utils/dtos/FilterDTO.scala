package utils.dtos

import scala.collection.mutable.ListBuffer

case class FilterDTO
(
  userName: String,
  sampleFakeId: Int,
  filters: ListBuffer[FieldDTO]
)
