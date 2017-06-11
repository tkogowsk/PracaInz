package utils

case class FilteringCounter
(
  filterName: String,
  relation: String,
  value: String,
  variant_column_id: Int
)

case class FilteringCountersDTO
(
  counters: List[FilteringCounter],
  sampleFakeId: Int,
  tabName: String
)