package repository

import javax.inject.Singleton

import models.{PrivilegeModel, UserModel}
import utils.MyPostgresDriver.api._

import scala.concurrent.Future

@Singleton
class PrivilegeRepository {
  private val db = Database.forConfig("postgresConf")

  var privilege = TableQuery[PrivilegeTableRepository]
  var sampleMetadata = SampleMetadataRepository.sampleMetadata

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

  def getAll(user: UserModel): Future[Seq[(String, Int)]] = {
    val query1 = for {
      (a, b) <- sampleMetadata join privilege on ((j, h) => j.sample_id === h.smpl_id && h.user_id === user.id)
    } yield (a.sample_id, a.fake_id)
    val a = query1.result
    db.run(a)
  }
}
