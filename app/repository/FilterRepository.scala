package repository

import models.FilterModel
import utils.MyPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, _}

object FilterRepository {

  private val db = Database.forConfig("postgresConf")

  var filter = TableQuery[FilterTableRepository]

  class FilterTableRepository(tag: Tag) extends Table[FilterModel](tag, "filter") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[Option[String]]("name")

    def * = (id, name) <> (FilterModel.tupled, FilterModel.unapply)

  }

  def getAll: Future[List[FilterModel]] = db.run {
    filter.to[List].result
  }

  def getIDByName(filterName: String): Option[Int] = {
    var id = None: Option[Int]
    Await.result({
      db.run {
        filter.filter(_.name === filterName).result.headOption
      }
        .map { value =>
          if (value.isDefined) {
            id = Option(value.get.id)
          }
        }
    }, Duration.Inf)
    id
  }

  def save(filterName: String) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "filter"("name")
              VALUES (${filterName})""")
    }, Duration.Inf)
  }

}
