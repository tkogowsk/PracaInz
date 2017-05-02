package repository

import javax.inject.Singleton

import models.FieldModel

import utils.MyPostgresDriver.api._

import scala.concurrent._

object FieldRepository {

  private val db = Database.forConfig("postgresConf")

  var field = TableQuery[FieldTableRepository]

  class FieldTableRepository(tag: Tag) extends Table[FieldModel](tag, "field") {

    def id = column[Int]("id", O.PrimaryKey)

    def variant_column_id = column[Int]("variant_column_id")

    def options = column[Option[List[String]]]("options")

    def relation = column[String]("relation")

    def * = (id, variant_column_id, options, relation) <> (FieldModel.tupled, FieldModel.unapply)

  }

  def getAll: Future[List[FieldModel]] = db.run {
    field.to[List].result
  }

}
