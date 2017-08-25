package utils.services

import javax.inject.{Inject, Singleton}

import repository._
import utils.MyPostgresDriver.api._
import utils.dtos.UploadRowDTO

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

@Singleton
class AddFilterService @Inject()(variantColumnRepository: VariantColumnRepository, tabFieldFilterRepository: TabFieldFilterRepository) {
  private val db = Database.forConfig("postgresConf")

  var tab = TabRepository.tab
  var field = FieldRepository.field
  var filter = FilterRepository.filter


  def clearData(): Unit = {
    Await.result({
      db.run(sqlu"""TRUNCATE TABLE "user_smp_tab" CASCADE""")
      db.run(sqlu"""TRUNCATE TABLE "field" CASCADE""")
      db.run(sqlu"""TRUNCATE TABLE "filter" CASCADE""")
      db.run(sqlu"""TRUNCATE TABLE "tab" CASCADE""")
      db.run(sqlu"""TRUNCATE TABLE "tab_field_filter" CASCADE""")
    }, Duration.Inf)
  }

  def save(rows: ArrayBuffer[UploadRowDTO]): Unit = {
    clearData()
    rows.foreach { row =>
      var tabID = addTab(row.tabName)
      var filterID = addFilter(row.filterName)
      var fieldID = addField(row.variantColumnName, row.options, row.relation)
      var defaultValue: Option[String] = None
      if (row.defaultValue.length > 0) {
        defaultValue = Some(row.defaultValue)
      }
      tabFieldFilterRepository.save(tabID, fieldID, filterID, defaultValue)
    }
  }

  def addTab(tabName: String): Int = {
    var tabID = TabRepository.getIDByName(tabName)
    if (tabID.isEmpty) {
      TabRepository.save(tabName)
      tabID = TabRepository.getIDByName(tabName)
    }
    tabID.get
  }

  def addFilter(tabName: String): Int = {
    var filterID = FilterRepository.getIDByName(tabName)
    if (filterID.isEmpty) {
      FilterRepository.save(tabName)
      filterID = FilterRepository.getIDByName(tabName)
    }
    filterID.get
  }

  def addField(variantColumnName: String, options: String, relation: String): Int = {
    var variantColumnId = variantColumnRepository.getIdByFName(variantColumnName)
    if (variantColumnId.isEmpty) {
      throw new IllegalArgumentException()
    }
    var fieldID = FieldRepository.getIDByFields(variantColumnId.get, options, relation)
    if (fieldID.isEmpty) {
      FieldRepository.save(variantColumnId.get, options, relation)
      fieldID = FieldRepository.getIDByFields(variantColumnId.get, options, relation)
    }
    fieldID.get
  }

}
