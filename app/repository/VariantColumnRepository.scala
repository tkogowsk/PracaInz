package repository

import javax.inject.Singleton

import models.VariantColumnModel
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration

@Singleton
class VariantColumnRepository {

private val db = Database.forConfig("postgresConf")

  var variant_columns = TableQuery[VariantColumnTableRepository]

  class VariantColumnTableRepository(tag: Tag) extends Table[VariantColumnModel](tag, "variant_column") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def columnName: Rep[String] = column[String]("column_name")

    def feName: Rep[String] = column[String]("fe_name")

    def columnType: Rep[String] = column[String]("type")

    def * = (id, columnName, feName, columnType) <> (VariantColumnModel.tupled, VariantColumnModel.unapply)

  }

  def getAll: Future[List[VariantColumnModel]] = db.run {
    variant_columns.to[List].result
  }

  def getIdByFName(feName: String): Option[Int] = {
    var id = None: Option[Int]
    Await.result({
      db.run {
        variant_columns.filter(_.feName === feName).result.headOption
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
