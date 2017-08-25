package utils.dtos

case class FilteringCounter
(
  filterName: String,
  relation: String,
  value: String,
  variantColumnId: Int
)

case class FilteringCountersDTO
(
  counters: List[FilteringCounter],
  sampleFakeId: Int,
  tabName: String
)