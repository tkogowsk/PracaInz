package repository


import models.SampleMetadataModel
import utils.MyPostgresDriver.api._

import scala.concurrent.Future

object SampleMetadataRepository {

  private val db = Database.forConfig("postgresConf")

  var sampleMetadata = TableQuery[SampleMetadataTableRepository]

  class SampleMetadataTableRepository(tag: Tag) extends Table[SampleMetadataModel](tag, "sample_metadata") {

    def sample_id = column[String]("sample_id", O.PrimaryKey)

    def fake_id = column[Int]("fake_id")

    def * = (sample_id, fake_id) <> (SampleMetadataModel.tupled, SampleMetadataModel.unapply)

  }

  def getAll(): Future[List[SampleMetadataModel]] = db.run {
    sampleMetadata.to[List].result
  }

}
