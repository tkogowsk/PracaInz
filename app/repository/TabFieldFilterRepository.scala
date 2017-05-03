package repository

import utils.MyPostgresDriver.api._
import javax.inject.Singleton

import models.TabFieldFilterModel

import scala.concurrent.Future

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

    def default_value = column[Option[List[String]]]("default_value")

    def * = (tab_id, field_id, filter_id, default_value) <> (TabFieldFilterModel.tupled, TabFieldFilterModel.unapply)

  }

  def getAll: Future[List[TabFieldFilterModel]] = db.run {
    tabFieldFilter.to[List].result
  }

  def getFilter: Future[Seq[(Int,Int,Int,Option[List[String]],String, Int, Option[List[String]], String, Option[String], Option[Boolean], Option[String], Option[String])]] = {
    val query1 = for {
      ((((a,b),c),d),e) <-  tabFieldFilter join tab on (_.tab_id === _.id) join field on (_._1.field_id === _.id) join filter on (_._1._1.filter_id === _.id) joinLeft userSmpTab on ((j,h) => {j._1._1._1.tab_id === h.tab_field_filter_tab_id && j._1._1._1.field_id === h.tab_field_filter_field_id && j._1._1._1.filter_id === h.tab_field_filter_filter_id}) //_._1._1._1.tab_id === _.tab_field_filter_tab_id)  on (_._1._1._1.field_id === _.tab_field_filter_field_id)
    } yield (a.tab_id, a.field_id, a.filter_id, a.default_value, b.name, c.variant_column_id, c.options, c.relation, d.name, d.is_global, e.map(_.value), e.map(_.smpl_id))

    val a = query1.result

    db.run(a)
  }
}

// (((a,b),c),d) <-  tabFieldFilter join tab on (_.tab_id === _.id) join field on (_._1.field_id === _.id) join filter on (_._1._1.filter_id === _.id)
//      ((((a,b),c),d),e) <-  tabFieldFilter join tab on (_.tab_id === _.id) join field on (_._1.field_id === _.id) join filter on (_._1._1.filter_id === _.id) joinLeft userSmpTab on ((j,h) => {j._1._1._1.tab_id === h.tab_field_filter_tab_id && j._1._1._1.field_id === h.tab_field_filter_field_id && j._1._1._1.filter_id === h.tab_field_filter_filter_id}) //_._1._1._1.tab_id === _.tab_field_filter_tab_id)  on (_._1._1._1.field_id === _.tab_field_filter_field_id)
//      ((a,b,c,d,e,f,g,h,i,j), k) <- query1 joinLeft userSmpTab on (_._1 === _.tab_field_filter_tab_id && _._2 === _.tab_field_filter_field_id && _._3 === _.tab_field_filter_filter_id)


/*def getFilter: Future[Seq[(Int,Int,Int,Option[List[String]],String, Int, Option[List[String]], String, Option[String], Option[Boolean], String)]] = {
    val query = for {
      a <- tabFieldFilter
      b <- tab
      c <- field
      d <- filter
      e <- userSmpTab
      if a.tab_id === b.id && a.field_id === c.id && a.filter_id === d.id && e.tab_field_filter_tab_id === a.tab_id && e.tab_field_filter_field_id === a.field_id && e.tab_field_filter_filter_id === a.filter_id
    } yield (a.tab_id, a.field_id, a.filter_id, a.default_value, b.name, c.variant_column_id, c.options, c.relation, d.name, d.is_global, e.value)
    val a = query.result
    db.run(a)
  }*/

/*
    val query2 = for {
      ((a,b,c,d,e,f,g,h,i,j), k) <- query1 joinLeft userSmpTab on ((x,y) => {}_._1 === _.tab_field_filter_tab_id && _._2 === _.tab_field_filter_field_id && _._3 === _.tab_field_filter_filter_id)
    } yield (a,b,c,d,e,f,g,h,i,j, k.map(_.value))*/