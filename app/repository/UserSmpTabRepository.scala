package repository

import models.UserSmpTabModel
import utils.MyPostgresDriver.api._
import utils.dtos.UserSampleTabDTO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object UserSmpTabRepository {
  private val db = Database.forConfig("postgresConf")

  var userSmpTab = TableQuery[UserSmpTabTableRepository]

  class UserSmpTabTableRepository(tag: Tag) extends Table[UserSmpTabModel](tag, "user_smp_tab") {

    def smpl_id = column[String]("sample_metadata_id", O.PrimaryKey)

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

  def save(userId: Int, dtos: List[UserSampleTabDTO]): Unit = {

    dtos.foreach((dto: UserSampleTabDTO) => {
      Await.result(
        SampleMetadataRepository.getByFakeId(dto.sample_fake_id).map {
          sample =>
            if (sample.isDefined) {
              val updateAction = userSmpTab.insertOrUpdate(UserSmpTabModel(sample.get.sample_id, userId, dto.tab_id, dto.field_id, dto.filter_id, dto.value))
              db.run(updateAction)
            }
        }, Duration.Inf)
    })
  }

}
