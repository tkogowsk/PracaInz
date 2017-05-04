package utils

import scala.collection.mutable.ListBuffer

case class DataRowDTO
(
  // row: scala.collection.mutable.Map[String, String]
  row: ListBuffer[DataCellDTO]
)