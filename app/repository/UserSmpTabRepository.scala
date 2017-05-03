package repository

import models.UserSmpTabModel

import scala.concurrent.Future
import utils.MyPostgresDriver.api._

object UserSmpTabRepository {
  private val db = Database.forConfig("postgresConf")

  var userSmpTab = TableQuery[UserSmpTabTableRepository]

  class UserSmpTabTableRepository(tag: Tag) extends Table[UserSmpTabModel](tag, "user_smp_tab") {

    def smpl_id = column[String]("smpl_id", O.PrimaryKey)

    def user_id = column[Int]("user_id", O.PrimaryKey)

    def tab_field_filter_tab_id = column[Int]("tab_field_filter_tab_id", O.PrimaryKey)

    def tab_field_filter_field_id = column[Int]("tab_field_filter_field_id", O.PrimaryKey)

    def tab_field_filter_filter_id = column[Int]("tab_field_filter_filter_id", O.PrimaryKey)

    def value = column[String]("value")

    def * = (smpl_id, user_id, tab_field_filter_tab_id, tab_field_filter_field_id, tab_field_filter_filter_id, value) <> (UserSmpTabModel.tupled, UserSmpTabModel.unapply)

  }

  def getAll: Future[List[UserSmpTabModel]] = db.run {
    userSmpTab.to[List].result
  }
}
