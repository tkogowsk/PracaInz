package repository

import utils.MyPostgresDriver.api._
import javax.inject.Singleton

import models.TabModel

import scala.concurrent.Future

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

}
