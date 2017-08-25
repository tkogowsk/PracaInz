package repository


import models.FieldModel
import utils.MyPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration

object FieldRepository {

  private val db = Database.forConfig("postgresConf")

  var field = TableQuery[FieldTableRepository]

  class FieldTableRepository(tag: Tag) extends Table[FieldModel](tag, "field") {

    def id = column[Int]("id", O.PrimaryKey)

    def variantColumnId = column[Int]("variant_column_id")

    def options = column[Option[String]]("options")

    def relation = column[String]("relation")

    def * = (id, variantColumnId, options, relation) <> (FieldModel.tupled, FieldModel.unapply)

  }

  def getAll: Future[List[FieldModel]] = db.run {
    field.to[List].result
  }


  def save(variantColumnId: Int, options: String, relation: String) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "field"("variant_column_id", "options", "relation")
              VALUES (${variantColumnId},${options},${relation})""")
    }, Duration.Inf)
  }

  def getIDByFields(variantColumnId: Int, options: String, relation: String): Option[Int] = {
    var id = None: Option[Int]
    Await.result({
      db.run {
        field.filter(field => field.variantColumnId === variantColumnId && field.options === options && field.relation === relation).result.headOption
      }
        .map { value =>
          if (value.isDefined) {
            id = Option(value.get.id)
          }
        }
    }, Duration.Inf)
    id
  }

}
