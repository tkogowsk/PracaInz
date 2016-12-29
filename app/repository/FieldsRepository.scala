package repository

import javax.inject.{Inject, Singleton}

import models.FieldsModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import shapeless._
import slickless._

@Singleton
class FieldsRepository {
  private val db = Database.forConfig("postgresConf")

  var fields = TableQuery[FieldsTableRepository]

  class FieldsTableRepository(tag: Tag) extends Table[FieldsModel](tag, "FIELDS") {
    def id = column[Int]("ID", O.PrimaryKey)

    def columnName = column[String]("COLUMN_NAME")

    def feName = column[String]("FE_NAME")

    def relation = column[String]("RELATION")

    def * = (id :: columnName :: feName :: relation :: HNil).mappedWith(Generic[FieldsModel])

  }

  def getAll(): Future[List[FieldsModel]] = db.run {
    fields.to[List].result
  }

}
