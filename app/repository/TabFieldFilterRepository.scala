package repository

import utils.MyPostgresDriver.api._
import javax.inject.Singleton

import models.TabFieldFilterModel
import utils.TabFieldFilterDTO

import scala.concurrent.Future

@Singleton
class TabFieldFilterRepository {

  private val db = Database.forConfig("postgresConf")

  var tabFieldFilter = TableQuery[TabFieldFilterTableRepository]

  var tabRepo = TabRepository.tab

  class TabFieldFilterTableRepository(tag: Tag) extends Table[TabFieldFilterModel](tag, "tab_field_filter") {

    def tab_id = column[Int]("tab_id", O.PrimaryKey)

    def field_id = column[Int]("field_id", O.PrimaryKey)

    def filter_id = column[Int]("filter_id", O.PrimaryKey)

    def default_value = column[Option[List[String]]]("default_value")


    def * = (tab_id, field_id, filter_id, default_value) <> (TabFieldFilterModel.tupled, TabFieldFilterModel.unapply)

  }

  def getAll: Future[List[TabFieldFilterModel]] = db.run {
    tabFieldFilter.to[List].result
  }

  def getJoin: Future[Seq[(Int,Int,Int,Option[List[String]],String)]] = {
    val query = for {
      (a,b) <-  tabFieldFilter join tabRepo on (_.tab_id === _.id)
    } yield (a.tab_id, a.field_id, a.filter_id, a.default_value, b.name)
    val a = query.result
    db.run(a)
  }
}
