package utils.services

import javax.inject.Singleton

import repository.{FieldRepository, FilterRepository, TabRepository}
import utils.MyPostgresDriver.api._
import utils.dtos.UploadRowDTO

import scala.collection.mutable.ArrayBuffer

@Singleton
class AddFilterService {
  private val db = Database.forConfig("postgresConf")

  var tab = TabRepository.tab
  var field = FieldRepository.field
  var filter = FilterRepository.filter

  def save(rows: ArrayBuffer[UploadRowDTO]): Unit = {
    rows.foreach { row =>
      var tabID = TabRepository.getIDByName(row.tabName)
      if (tabID.isDefined) {
      }
      else {
        TabRepository.save(row.tabName)
        tabID = TabRepository.getIDByName(row.tabName)

      }
    }
  }

}
