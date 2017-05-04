package repository

import javax.inject.Inject

import models.VariantColumnModel
import play.api.db.{Database, NamedDatabase}
import utils.{DataCellDTO, DataRowDTO}

import scala.collection.mutable.ListBuffer

class JDBCRepository @Inject()(@NamedDatabase("jdbcConf") db: Database) {

  def getAll(columns: List[VariantColumnModel]): ListBuffer[DataRowDTO] = {
    val list = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val queryEnd = "from transcript"
    var queryColumns = ""

    columns.foreach(elem =>
      queryColumns += ",\"" + elem.column_name + "\" "
    )
    queryColumns = queryColumns.substring(1)

    val query = queryBeginning + queryColumns + queryEnd

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)

      while (rs.next()) {
        val list2 = ListBuffer[DataCellDTO]()
        columns.foreach(elem =>
          list2 += DataCellDTO(elem.column_name, rs.getString(elem.column_name))
        )
        list += DataRowDTO(list2)
      }

    } finally {
      conn.close()
    }

    list
  }
}

