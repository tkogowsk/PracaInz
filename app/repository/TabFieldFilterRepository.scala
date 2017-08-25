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

    def tabId = column[Int]("tab_id", O.PrimaryKey)

    def fieldId = column[Int]("field_id", O.PrimaryKey)

    def filterId = column[Int]("filter_id", O.PrimaryKey)

    def defaultValue = column[Option[String]]("default_value")

    def * = (tabId, fieldId, filterId, defaultValue) <> (TabFieldFilterModel.tupled, TabFieldFilterModel.unapply)

  }

  def getAll: Future[List[TabFieldFilterModel]] = db.run {
    tabFieldFilter.to[List].result
  }

  def getFilter(smplId: String, userId: Int): Future[Seq[(Int, Int, Int, Option[String], String, Int, Option[String], String, Option[String], Option[String], Option[String])]] = {
    val query = for {
      ((((a, b), c), d), e) <- tabFieldFilter join tab on (_.tabId === _.id) join field on (_._1.fieldId === _.id) join filter on (_._1._1.filterId === _.id) joinLeft userSmpTab on ((j, h) => {
        j._1._1._1.tabId === h.tabFieldFilterTabId && j._1._1._1.fieldId === h.tabFieldFilterFieldId && j._1._1._1.filterId === h.tabFieldFilterFilterId && h.userId === userId && h.smplId === smplId
      })
    } yield (a.tabId, a.fieldId, a.filterId, a.defaultValue, b.name, c.variantColumnId, c.options, c.relation, d.name, e.map(_.value), e.map(_.smplId))

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
