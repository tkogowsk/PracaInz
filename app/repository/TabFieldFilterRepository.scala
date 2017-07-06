package repository

import javax.inject.Singleton

import models.TabFieldFilterModel
import utils.MyPostgresDriver.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@Singleton
class TabFieldFilterRepository {

  private val db = Database.forConfig("postgresConf")

  var tabFieldFilter = TableQuery[TabFieldFilterTableRepository]

  var tab = TabRepository.tab
  var field = FieldRepository.field
  var filter = FilterRepository.filter
  var userSmpTab = UserSmpTabRepository.userSmpTab

  class TabFieldFilterTableRepository(tag: Tag) extends Table[TabFieldFilterModel](tag, "tab_field_filter") {

    def tab_id = column[Int]("tab_id", O.PrimaryKey)

    def field_id = column[Int]("field_id", O.PrimaryKey)

    def filter_id = column[Int]("filter_id", O.PrimaryKey)

    def default_value = column[Option[String]]("default_value")

    def * = (tab_id, field_id, filter_id, default_value) <> (TabFieldFilterModel.tupled, TabFieldFilterModel.unapply)

  }

  def getAll: Future[List[TabFieldFilterModel]] = db.run {
    tabFieldFilter.to[List].result
  }

  def getFilter(smpl_id: String, user_id: Int): Future[Seq[(Int, Int, Int, Option[String], String, Int, Option[String], String, Option[String], Option[Boolean], Option[String], Option[String])]] = {
    val query = for {
      ((((a, b), c), d), e) <- tabFieldFilter join tab on (_.tab_id === _.id) join field on (_._1.field_id === _.id) join filter on (_._1._1.filter_id === _.id) joinLeft userSmpTab on ((j, h) => {
        j._1._1._1.tab_id === h.tab_field_filter_tab_id && j._1._1._1.field_id === h.tab_field_filter_field_id && j._1._1._1.filter_id === h.tab_field_filter_filter_id && h.user_id === user_id && h.smpl_id === smpl_id
      })
    } yield (a.tab_id, a.field_id, a.filter_id, a.default_value, b.name, c.variant_column_id, c.options, c.relation, d.name, d.is_global, e.map(_.value), e.map(_.smpl_id))

    val a = query.result

    db.run(a)
  }

  def save(tabID: Int, fieldID: Int, filterID: Int, defaultValue: Option[String]) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "tab_field_filter"("tab_id", "field_id","filter_id","default_value")
              VALUES (${tabID}, ${fieldID}, ${filterID}, ${defaultValue})""")
    }, Duration.Inf)
  }

}
