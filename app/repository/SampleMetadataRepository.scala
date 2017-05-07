package repository

import javax.inject.Singleton

import models.SampleMetadataModel
import utils.MyPostgresDriver.api._

import scala.concurrent.Future

@Singleton
class SampleMetadataRepository {
  private val db = Database.forConfig("postgresConf")
  var sampleMetadata = TableQuery[SampleMetadataTableRepository]

  class SampleMetadataTableRepository(tag: Tag) extends Table[SampleMetadataModel](tag, "sample_metadata") {

    def sample_id = column[String]("sample_id", O.PrimaryKey)

    def * = (sample_id) <> (SampleMetadataModel.apply, SampleMetadataModel.unapply)

  }

  def getAll(): Future[List[SampleMetadataModel]] = db.run {
    sampleMetadata.to[List].result
  }

}
