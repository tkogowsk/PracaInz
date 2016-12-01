package repository

import javax.inject.{Inject, Singleton}

import models.TranscriptModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import shapeless._
import slickless._

@Singleton
class TranscriptRepository {
  private val db = Database.forConfig("postgresConf")

  class TranscriptTableRepository(tag: Tag) extends Table[TranscriptModel](tag, "Transcripts") {
    def chrom = column[Int]("Chrom")

    def position = column[Int]("Position")

    def rsid = column[String]("RSID")

    def reference = column[String]("Reference")

    def alternate = column[String]("Alternate")

    def consequence = column[String]("Consequence")

    def proteinConsequence = column[String]("Protein Consequence")

    def transcriptConsequence = column[String]("Transcript Consequence")

    def filter = column[String]("Filter")

    def annotation = column[String]("Annotation")

    def flags = column[String]("Flags")

    def alleleCount = column[Int]("Allele Count")

    def alleleNumber = column[Int]("Allele Number")

    def alleleFrequency = column[Double]("Allele Frequency")

    def numberOfHomozygotes = column[Double]("Number of Homozygotes")

    def alleleCountAfrican = column[Double]("Allele Count African")

    def alleleNumberAfrican = column[Double]("Allele Number African")

    def homozygoteCountAfrican = column[Double]("Homozygote Count African")

    def alleleCountEastAsian = column[Double]("Allele Count East Asian")

    def alleleNumberEastAsian = column[Double]("Allele Number East Asian")

    def homozygoteCountEastAsian = column[Double]("Homozygote Count East Asian")

    def alleleCountEuropeanNonFinnish = column[Double]("Allele Count European (Non-Finnish)")

    def alleleNumberEuropeanNonFinnish = column[Double]("Allele Number European (Non-Finnish)")

    def homozygoteCountEuropeanNonFinnish = column[Double]("Homozygote Count European (Non-Finnish)")

    def alleleCountFinnish = column[Double]("Allele Count Finnish")

    def alleleNumberFinnish = column[Double]("Allele Number Finnish")

    def homozygoteCountFinnish = column[Double]("Homozygote Count Finnish")

    def alleleCountLatino = column[Double]("Allele Count Latino")

    def alleleNumberLatino = column[Double]("Allele Number Latino")

    def homozygoteCountLatino = column[Double]("Homozygote Count Latino")

    def alleleCountOther = column[Double]("Allele Count Other")

    def alleleNumberOther = column[Double]("Allele Number Other")

    def homozygoteCountOther = column[Double]("Homozygote Count Other")

    def alleleCountSouthAsian = column[Double]("Allele Count South Asian")

    def alleleNumberSouthAsian = column[Double]("Allele Number South Asian")

    def homozygoteCountSouthAsian = column[Double]("Homozygote Count South Asian")

    def id = column[Int]("id", O.PrimaryKey)


    def * = (chrom :: position :: rsid ::
      reference :: alternate :: consequence ::
      proteinConsequence :: transcriptConsequence :: filter ::
      annotation :: flags :: alleleCount ::
      alleleNumber :: alleleFrequency :: numberOfHomozygotes ::
      alleleCountAfrican :: alleleNumberAfrican :: homozygoteCountAfrican ::
      alleleCountEastAsian :: alleleNumberEastAsian :: homozygoteCountEastAsian ::
      alleleCountEuropeanNonFinnish :: alleleNumberEuropeanNonFinnish :: homozygoteCountEuropeanNonFinnish ::
      alleleCountFinnish :: alleleNumberFinnish :: homozygoteCountFinnish ::
      alleleCountLatino :: alleleNumberLatino :: homozygoteCountLatino ::
      alleleCountOther :: alleleNumberOther :: homozygoteCountOther ::
      alleleCountSouthAsian :: alleleNumberSouthAsian ::
      homozygoteCountSouthAsian :: id :: HNil).mappedWith(Generic[TranscriptModel])
    /*//<> (TranscriptModel.tupled, TranscriptModel.unapply)*/

  }

  var transcripts = TableQuery[TranscriptTableRepository]

  def getAll(): Future[List[TranscriptModel]] = db.run {
    transcripts.to[List].result
  }

}
