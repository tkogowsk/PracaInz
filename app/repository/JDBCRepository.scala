package repository

import javax.inject.Inject

import models.VariantColumnModel
import play.api.db.{Database, NamedDatabase}
import utils._
import utils.dtos._
import utils.services.ConfigService

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import java.sql.PreparedStatement

class JDBCRepository @Inject()(@NamedDatabase("jdbcConf") db: Database, variantColumnRepository: VariantColumnRepository) {

  def getAllIds: ListBuffer[String] = {
    val result = ListBuffer[String]()
    val conn = db.getConnection()
    val queryBeginning = "select distinct sample_id "
    val queryEnd = "from " + ConfigService.getTranscriptTableName

    val query = queryBeginning +  queryEnd

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)

      while (rs.next()) {
        result += rs.getString("sample_id")
      }

    } finally {
      conn.close()
    }

    result
  }

  def getAll(columns: List[VariantColumnModel]): ListBuffer[DataRowDTO] = {
    val result = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val queryEnd = "from " + ConfigService.getTranscriptTableName
    var queryColumns = ""

    columns.foreach(elem =>
      queryColumns += ",\"" + elem.columnName + "\" "
    )
    queryColumns = queryColumns.substring(1)

    val query = queryBeginning + queryColumns + queryEnd

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)

      while (rs.next()) {
        val item = ListBuffer[DataCellDTO]()
        columns.foreach(elem =>
          item += DataCellDTO(elem.id, elem.columnName, rs.getString(elem.columnName))
        )
        result += DataRowDTO(item)
      }

    } finally {
      conn.close()
    }

    result
  }

  def getBySampleId(sampleId: String): ListBuffer[DataRowDTO] = {
    val columns: List[VariantColumnModel] = this.getColumns
    val result = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
    val queryEnd = "from " + ConfigService.getTranscriptTableName + " where \"" + transcriptTableSampleIdColumnName + "\" = '" + sampleId + "'"
    var queryColumns = ""

    columns.foreach(elem =>
      queryColumns += ",\"" + elem.columnName + "\" "
    )
    queryColumns = queryColumns.substring(1)

    val query = queryBeginning + queryColumns + queryEnd

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)

      while (rs.next()) {
        val item = ListBuffer[DataCellDTO]()
        columns.foreach(elem =>
          item += DataCellDTO(elem.id, elem.columnName, rs.getString(elem.columnName))
        )
        result += DataRowDTO(item)
      }

    } finally {
      conn.close()
    }

    result
  }

  def getColumns: List[VariantColumnModel] = {
    var columns: List[VariantColumnModel] = List[VariantColumnModel]()
    Await.result(
      variantColumnRepository.getAll.map {
        res => columns = res
      }, Duration.Inf)

    return columns
  }

  def setPreparedStatementValues(preparedStatement: PreparedStatement, fields: ListBuffer[FieldDTO], columns: List[VariantColumnModel]) = {
    for( i <- 1 to fields.size) {
      val field = fields(i - 1)
      val columnType = filterColumn(columns, field.variantColumnId).columnType
      columnType match {
        case "text" => preparedStatement.setString(i, field.value)
        case "string" => preparedStatement.setString(i, field.value)
        case "int" => preparedStatement.setInt(i, field.value.toInt)
        case "double" => preparedStatement.setDouble(i, field.value.toDouble)
      }
    }
  }


  def getByFields(filter: FilterDTO, sampleId: String): ListBuffer[DataRowDTO] = {
    if (filter.filters.isEmpty) {
      return getBySampleId(sampleId)
    }
    val columns: List[VariantColumnModel] = this.getColumns

    var preparedStatement: PreparedStatement = null
    val result = ListBuffer[DataRowDTO]()
    val dbConnection = db.getConnection()
    val queryBeginning = "SELECT "
    val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
    val queryEnd = "from " + ConfigService.getTranscriptTableName
    val queryColumns = getColumnQueryPart(columns)
    val queryWhere = addAndSampleIdCondition(getWhereQueryPart(columns, filter.filters), transcriptTableSampleIdColumnName, sampleId)

    val query = queryBeginning + queryColumns + queryEnd + queryWhere

    try {
      preparedStatement = dbConnection.prepareStatement(query)
      setPreparedStatementValues(preparedStatement, filter.filters, columns)

      val rs = preparedStatement.executeQuery()

      while (rs.next()) {
        val item = ListBuffer[DataCellDTO]()
        columns.foreach(elem =>
          item += DataCellDTO(elem.id, elem.columnName, rs.getString(elem.columnName))
        )
        result += DataRowDTO(item)
      }

    } catch {
      case e: Exception => println(e)
    } finally {
      dbConnection.close()
    }

    result
  }

  def getColumnQueryPart(columns: List[VariantColumnModel]): String = {
    var queryColumns = ""
    columns.foreach(elem =>
      queryColumns += ",\"" + elem.columnName + "\" "
    )
    queryColumns = queryColumns.substring(1)
    queryColumns
  }

  def getWhereQueryPart(columns: List[VariantColumnModel], fields: ListBuffer[FieldDTO]): String = {
    var query = " WHERE "

    fields.foreach(field => {
      val columnName = filterColumn(columns, field.variantColumnId).columnName

      field.relation.toString match {
        case Relation.Greater => query = query.concat("\"" + columnName + "\"" + " > ? AND ")
        case Relation.GreaterThan => query = query.concat("\"" + columnName + "\"" + " >= ? AND ")
        case Relation.Equals => query = query.concat("\"" + columnName + "\"" + " = ? AND ")
        case Relation.Less => query = query.concat("\"" + columnName + "\"" + " < ? AND ")
        case Relation.LessThan => query = query.concat("\"" + columnName + "\"" + " <= ? AND ")
      }
    })
    // remove last " AND " string
    query = query.slice(0, query.length - 5)
    query
  }

  def addAndSampleIdCondition(query: String, transcriptTableSampleIdColumnName: String, sampleId: String): String = {
    query + " AND \"" + transcriptTableSampleIdColumnName + "\" = '" + sampleId + "'"
  }

  def getSampleIdCondition(transcriptTableSampleIdColumnName: String, sampleId: String): String = {
    " " + transcriptTableSampleIdColumnName + " = '" + sampleId + "'"
  }

  def filterColumn(columns: List[VariantColumnModel], columnID: Int): VariantColumnModel = {
    columns.find(elem => elem.id == columnID).get
  }

  def count(dto: FilteringCountersDTO) = {
    val columns: List[VariantColumnModel] = this.getColumns
    var response = ListBuffer[FilteringCountersDTOResponse]()
    val mapOfFilters: mutable.Map[String, ListBuffer[FieldDTO]] = new mutable.LinkedHashMap
    var sampleId = None: Option[String]
    Await.result(
      SampleMetadataRepository.getByFakeId(dto.sampleFakeId).map {
        sample =>
          if (sample.isDefined) {
            sampleId = Some(sample.get.sampleId)
          } else {
            throw new IllegalArgumentException()
          }
      }, Duration.Inf)

    dto.counters.foreach {
      elem => {
        var list: Option[ListBuffer[FieldDTO]] = mapOfFilters.get(elem.filterName)
        if (list.isEmpty) {
          list = Option(new ListBuffer[FieldDTO])
          mapOfFilters.put(elem.filterName, list.get)
        }
        list.get += FieldDTO(elem.relation, elem.value, elem.variantColumnId)
      }
    }
    val setOfFilterNames = mapOfFilters.keys.toArray
    var iterator: Int = 0
    val filters = setOfFilterNames.slice(0, iterator)

    for (i <- 0 until setOfFilterNames.length) {
      var names = setOfFilterNames.slice(0, i + 1)
      var fields: ListBuffer[FieldDTO] = new ListBuffer
      for (name <- names) {
        fields ++= mapOfFilters(name)
      }
      var preparedStatement: PreparedStatement = null
      val queryBeginning = "SELECT COUNT(1) AS count "
      val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
      val queryEnd = "from " + ConfigService.getTranscriptTableName
      val queryWhere = addAndSampleIdCondition(getWhereQueryPart(columns, fields), transcriptTableSampleIdColumnName, sampleId.get)

      val query = queryBeginning + queryEnd + queryWhere
      val dbConnection = db.getConnection()
      try {
        preparedStatement = dbConnection.prepareStatement(query)
        setPreparedStatementValues(preparedStatement, fields, columns)
        val rs = preparedStatement.executeQuery()

        while (rs.next()) {
          var count = rs.getInt("count")
          response += FilteringCountersDTOResponse(setOfFilterNames(i), count)
        }

      } catch {
        case e: Exception => println(e)
      } finally {
        dbConnection.close()
      }

    }

    response
  }

}

