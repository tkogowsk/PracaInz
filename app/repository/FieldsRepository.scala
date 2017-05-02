package repository

import javax.inject.{Inject, Singleton}

import models.FieldsModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import shapeless._
import slickless._
import utils.FieldSaveDTO
import scala.collection.mutable.ArrayBuffer

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

  def getAll: Future[List[FieldsModel]] = db.run {
    fields.to[List].result
  }

  def save(fieldsDto: ArrayBuffer[FieldSaveDTO]) =  {
    db.run(sqlu"""TRUNCATE TABLE "FORMS"""")
    db.run(sqlu"""TRUNCATE TABLE "FIELDS" RESTART IDENTITY CASCADE""")
    fieldsDto.foreach(elem => {
      db.run(
        sqlu"""insert into "FIELDS"("COLUMN_NAME","FE_NAME","RELATION")
              VALUES (${elem.columnName}, ${elem.feName}, ${elem.relation})""")
    })
  }
}
