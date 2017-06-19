package utils.dtos

import scala.collection.mutable.ListBuffer

case class DataRowDTO
(
  row: ListBuffer[DataCellDTO]
)