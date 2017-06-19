package repository

import models.TabModel
import utils.MyPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TabRepository {

  private val db = Database.forConfig("postgresConf")

  var tab = TableQuery[TabTableRepository]

  class TabTableRepository(tag: Tag) extends Table[TabModel](tag, "tab") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[String]("name")

    def * = (id, name) <> (TabModel.tupled, TabModel.unapply)

  }

  def getAll: Future[List[TabModel]] = db.run {
    tab.to[List].result
  }

  def getByName(tabName: String): Future[Option[TabModel]] = db.run {
    tab.filter(_.name === tabName).result.headOption
  }

  def getIDByName(tabName: String): Option[Int] = {
    var id = None: Option[Int]
    Await.result({
      db.run {
        tab.filter(_.name === tabName).result.headOption
      }
        .map { value =>
          if (value.isDefined) {
            id = Option(value.get.id)
          }
        }
    }, Duration.Inf)
    id
  }

  def save(tabName: String) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "tab"("name")
              VALUES (${tabName})""")
    }, Duration.Inf)

  }
}
