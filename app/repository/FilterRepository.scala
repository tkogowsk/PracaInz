package repository

import scala.concurrent.Future
import javax.inject.Singleton

import models.{FieldModel, FilterModel}
import utils.MyPostgresDriver.api._

import scala.concurrent._

object FilterRepository {

  private val db = Database.forConfig("postgresConf")

  var filter = TableQuery[FilterTableRepository]

  class FilterTableRepository(tag: Tag) extends Table[FilterModel](tag, "filter") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[Option[String]]("name")

    def is_global = column[Option[Boolean]]("is_global")

    def * = (id, name, is_global) <> (FilterModel.tupled, FilterModel.unapply)

  }

  def getAll: Future[List[FilterModel]] = db.run {
    filter.to[List].result
  }

}
