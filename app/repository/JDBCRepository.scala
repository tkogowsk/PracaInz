package repository

import javax.inject.Inject

import models.VariantColumnModel
import play.api.db.{Database, NamedDatabase}
import utils._

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

  def getBySampleId(columns: List[VariantColumnModel], sampleId: String): ListBuffer[DataRowDTO] = {
    val list = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
    val queryEnd = "from transcript where \"" + transcriptTableSampleIdColumnName + "\" = '" + sampleId + "'"
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

  def getByFields(columns: List[VariantColumnModel], filter: FilterDTO, sampleFakeId: String): ListBuffer[DataRowDTO] = {
    val list = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
    val queryEnd = "from transcript"
    val queryColumns = getColumnQueryPart(columns)
    val queryWhere = addSampleIdCondition(getWhereQueryPart(columns, filter), transcriptTableSampleIdColumnName, sampleFakeId)

    val query = queryBeginning + queryColumns + queryEnd + queryWhere

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

  def getColumnQueryPart(columns: List[VariantColumnModel]): String = {
    var queryColumns = ""
    columns.foreach(elem =>
      queryColumns += ",\"" + elem.column_name + "\" "
    )
    queryColumns = queryColumns.substring(1)
    queryColumns
  }

  def getWhereQueryPart(columns: List[VariantColumnModel], filter: FilterDTO): String = {
    var query = " WHERE "

    filter.filters.foreach(field => {
      val columnName = filterColumnName(columns, field.variant_column_id)


      field.relation.toString match {
        /* case Relation.Contains => {
           var values = userField.value.split(",", -1)
           var array = ArrayBuffer[String]()
           values.foreach(elem => array += ("'" + elem + "'"))
           query = query.concat("\"" + field.columnName.toString + "\"" + " IN(" + array.mkString(",") + ") AND ")
         }*/

        case Relation.Greater => query = query.concat("\"" + columnName + "\"" + " > \'" + field.value + "\' AND ")
        case Relation.GreaterThan => query = query.concat("\"" + columnName + "\"" + " >= \'" + field.value + "\' AND ")
        case Relation.Equals => query = query.concat("\"" + columnName + "\"" + " = \'" + field.value + "\' AND ")
        case Relation.Less => query = query.concat("\"" + columnName + "\"" + " < \'" + field.value + "\' AND ")
        case Relation.LessThan => query = query.concat("\"" + columnName + "\"" + " <= \'" + field.value + "\' AND ")
      }
    })
    query = query.slice(0, query.length - 5)
    query
  }

  def addSampleIdCondition(query: String, transcriptTableSampleIdColumnName: String, sampleFakeId: String): String = {
    query + " AND \"" + transcriptTableSampleIdColumnName + "\" = '" + sampleFakeId + "'"
  }
  def filterColumnName(columns: List[VariantColumnModel], columnID: Int): String = {
    columns.find(elem => elem.id == columnID).get.column_name
  }
}

