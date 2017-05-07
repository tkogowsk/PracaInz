package repository

import javax.inject.Singleton

import models.PrivilegeModel
import utils.MyPostgresDriver.api._

import scala.concurrent.Future

@Singleton
class PrivilegeRepository {
  private val db = Database.forConfig("postgresConf")
  var privilege = TableQuery[PrivilegeTableRepository]

  class PrivilegeTableRepository(tag: Tag) extends Table[PrivilegeModel](tag, "privilege") {

    def user_id = column[Int]("user_id", O.PrimaryKey)

    def smpl_id = column[String]("smpl_id", O.PrimaryKey)

    def access_type = column[Option[String]]("access_type")

    def region_id = column[Option[Int]]("region_id", O.PrimaryKey)

    def * = (user_id, smpl_id, access_type, region_id) <> (PrivilegeModel.tupled, PrivilegeModel.unapply)

  }

  def getAll(): Future[List[PrivilegeModel]] = db.run {
    privilege.to[List].result
  }
}
