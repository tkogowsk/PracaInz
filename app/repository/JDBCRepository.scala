package repository

import javax.inject.Inject

import models.VariantColumnModel
import play.api.db.{Database, NamedDatabase}
import utils._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class JDBCRepository @Inject()(@NamedDatabase("jdbcConf") db: Database, variantColumnRepository: VariantColumnRepository) {

  def getAll(columns: List[VariantColumnModel]): ListBuffer[DataRowDTO] = {
    val list = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val queryEnd = "from " + ConfigService.getTranscriptTableName
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

  def getBySampleId(sampleId: String): ListBuffer[DataRowDTO] = {
    val columns: List[VariantColumnModel] = this.getColumns
    val list = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
    val queryEnd = "from " + ConfigService.getTranscriptTableName + " where \"" + transcriptTableSampleIdColumnName + "\" = '" + sampleId + "'"
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

  def getColumns: List[VariantColumnModel] = {
    var columns: List[VariantColumnModel] = List[VariantColumnModel]()
    Await.result(
      variantColumnRepository.getAll.map {
        res => columns = res
      }, Duration.Inf)

    return columns
  }

  def getByFields(filter: FilterDTO, sampleId: String): ListBuffer[DataRowDTO] = {
    if (filter.filters.isEmpty) {
      return getBySampleId(sampleId)
    }
    val columns: List[VariantColumnModel] = this.getColumns

    val list = ListBuffer[DataRowDTO]()
    val conn = db.getConnection()
    val queryBeginning = "SELECT "
    val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
    val queryEnd = "from " + ConfigService.getTranscriptTableName
    val queryColumns = getColumnQueryPart(columns)
    val queryWhere = addSampleIdCondition(getWhereQueryPart(columns, filter.filters), transcriptTableSampleIdColumnName, sampleId)

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

    } catch {
      case e: Exception => println(e.getMessage)
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

  def getWhereQueryPart(columns: List[VariantColumnModel], fields: ListBuffer[FieldDTO]): String = {
    var query = " WHERE "

    fields.foreach(field => {
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

  def addSampleIdCondition(query: String, transcriptTableSampleIdColumnName: String, sampleId: String): String = {
    query + " AND \"" + transcriptTableSampleIdColumnName + "\" = '" + sampleId + "'"
  }

  def filterColumnName(columns: List[VariantColumnModel], columnID: Int): String = {
    columns.find(elem => elem.id == columnID).get.column_name
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
            sampleId = Some(sample.get.sample_id)
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
        list.get += FieldDTO(elem.relation, elem.value, elem.variant_column_id)
      }
    }
    val setOfFilterNames = mapOfFilters.keys.toArray
    var iterator: Int = 0
    val filters = setOfFilterNames.slice(0, iterator)

    for (i <- 0 to setOfFilterNames.length) {
      var names = setOfFilterNames.slice(0, i + 1)
      var fields: ListBuffer[FieldDTO] = new ListBuffer
      for (name <- names) {
        fields ++= mapOfFilters(name)
      }
      val queryBeginning = "SELECT COUNT(1) AS count "
      val transcriptTableSampleIdColumnName = ConfigService.getTranscriptTableSampleIdColumnName
      val queryEnd = "from " + ConfigService.getTranscriptTableName
      val queryWhere = addSampleIdCondition(getWhereQueryPart(columns, fields), transcriptTableSampleIdColumnName, sampleId.get)

      val query = queryBeginning + queryEnd + queryWhere
      val conn = db.getConnection()
      try {
        val stmt = conn.createStatement
        val rs = stmt.executeQuery(query)

        while (rs.next()) {
          var count = rs.getInt("count")
          response += FilteringCountersDTOResponse(setOfFilterNames(i), count)
        }

      } catch {
        case e: Exception => println(e.getMessage)
      } finally {
        conn.close()
      }

    }

    response
  }

}

