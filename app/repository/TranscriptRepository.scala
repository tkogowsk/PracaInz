package repository
import javax.inject.{Inject, Singleton}

import com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList
import models.TranscriptModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import ExecutionContext.Implicits.global

@Singleton
class TranscriptRepository {
  private val db = Database.forConfig("postgresConf")

  class TranscriptTableRepository(tag: Tag) extends Table[TranscriptModel](tag, "ExacCsv") {
    def chrom = column[Int]("Chrom")
    def position = column[Int]("Position")
    def rsid = column[String]("RSID")
    def reference = column[String]("Reference")
    def * = (chrom, position, rsid, reference)  <> (TranscriptModel.tupled, TranscriptModel.unapply)
  }

  var transcripts = TableQuery[TranscriptTableRepository]

  def getAll(): Future[List[TranscriptModel]] = db.run { transcripts.to[List].result }

}
