package utils.dtos

import scala.collection.mutable.ListBuffer

case class FilterDTO
(
  sampleFakeId: Int,
  filters: ListBuffer[FieldDTO]
)
