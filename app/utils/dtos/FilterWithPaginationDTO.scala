package utils.dtos

case class ColumnSortingDTO
(
  column_name: String,
  sorting: String
)

case class ColumnSearchDTO
(
  column_name: String,
  searchValue: String
)

case class FilterWithPaginationDTO
(
  filter: FilterDTO,
  sorting: ColumnSortingDTO,
  search: ColumnSearchDTO
)
