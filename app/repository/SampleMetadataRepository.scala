package repository


import models.SampleMetadataModel
import utils.MyPostgresDriver.api._

import scala.concurrent.Future

object SampleMetadataRepository {

  private val db = Database.forConfig("postgresConf")

  var sampleMetadata = TableQuery[SampleMetadataTableRepository]

  class SampleMetadataTableRepository(tag: Tag) extends Table[SampleMetadataModel](tag, "sample_metadata") {

    def sampleId = column[String]("sample_id", O.PrimaryKey)

    def fakeId = column[Int]("fake_id")

    def * = (sampleId, fakeId) <> (SampleMetadataModel.tupled, SampleMetadataModel.unapply)

  }

  def getAll(): Future[List[SampleMetadataModel]] = db.run {
    sampleMetadata.to[List].result
  }

  def getByFakeId(fakeId: Int): Future[Option[SampleMetadataModel]] = db.run {
    sampleMetadata.filter(_.fakeId === fakeId).result.headOption
  }

}
