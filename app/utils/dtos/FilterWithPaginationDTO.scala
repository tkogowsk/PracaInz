package utils.dtos

case class ColumnSortingDTO
(
  columnName: String,
  sorting: String
)

case class ColumnSearchDTO
(
  columnName: String,
  searchValue: String
)

case class FilterWithPaginationDTO
(
  filter: FilterDTO,
  sorting: ColumnSortingDTO,
  search: ColumnSearchDTO
)
