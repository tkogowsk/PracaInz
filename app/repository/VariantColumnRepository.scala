package repository

import javax.inject.Singleton

import models.VariantColumnModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._

@Singleton
class VariantColumnRepository {

private val db = Database.forConfig("postgresConf")

  var variant_columns = TableQuery[VariantColumnTableRepository]

  class VariantColumnTableRepository(tag: Tag) extends Table[VariantColumnModel](tag, "variant_column") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def column_name: Rep[String] = column[String]("column_name")

    def fe_name: Rep[String] = column[String]("fe_name")

    def column_type: Rep[String] = column[String]("type")

    def * = (id, column_name, fe_name, column_type) <> (VariantColumnModel.tupled, VariantColumnModel.unapply)

  }

  def getAll: Future[List[VariantColumnModel]] = db.run {
    variant_columns.to[List].result
  }

}
