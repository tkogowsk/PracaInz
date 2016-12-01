package repository

import javax.inject.{Inject, Singleton}

import models.UserFilterSelectionModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import shapeless._
import slickless._

@Singleton
class UserFilterSelectionRepository {
  private val db = Database.forConfig("postgresConf")

  class UserFilterSelectionRepository(tag: Tag) extends Table[UserFilterSelectionModel](tag, "User_Filter_Selection") {
    def chromIndc = column[Boolean]("Chrom Indc")

    def positionIndc = column[Boolean]("Position Indc")

    def rsidIndc = column[Boolean]("RSID Indc")

    def referenceIndc = column[Boolean]("Reference Indc")

    def alternateIndc = column[Boolean]("Alternate Indc")

    def consequenceIndc = column[Boolean]("Consequence Indc")

    def proteinConsequenceIndc = column[Boolean]("Protein Consequence Indc")

    def transcriptConsequenceIndc = column[Boolean]("Transcript Consequence Indc")

    def filterIndc = column[Boolean]("Filter Indc")

    def annotationIndc = column[Boolean]("Annotation Indc")

    def flagsIndc = column[Boolean]("Flags Indc")

    def alleleCountIndc = column[Boolean]("Allele Count Indc")

    def alleleNumberIndc = column[Boolean]("Allele Number Indc")

    def alleleFrequencyIndc = column[Boolean]("Allele Frequency Indc")

    def numberOfHomozygotesIndc = column[Boolean]("Number of Homozygotes Indc")

    def alleleCountAfricanIndc = column[Boolean]("Allele Count African Indc")

    def alleleNumberAfricanIndc = column[Boolean]("Allele Number African Indc")

    def homozygoteCountAfricanIndc = column[Boolean]("Homozygote Count African Indc")

    def alleleCountEastAsianIndc = column[Boolean]("Allele Count East Asian Indc")

    def alleleNumberEastAsianIndc = column[Boolean]("Allele Number East Asian Indc")

    def homozygoteCountEastAsianIndc = column[Boolean]("Homozygote Count East Asian Indc")

    def alleleCountEuropeanNonFinnishIndc = column[Boolean]("Allele Count European (Non-Finnish) Indc")

    def alleleNumberEuropeanNonFinnishIndc = column[Boolean]("Allele Number European (Non-Finnish) Indc")

    def homozygoteCountEuropeanNonFinnishIndc = column[Boolean]("Homozygote Count European (Non-Finnish) Indc")

    def alleleCountFinnishIndc = column[Boolean]("Allele Count Finnish Indc")

    def alleleNumberFinnishIndc = column[Boolean]("Allele Number Finnish Indc")

    def homozygoteCountFinnishIndc = column[Boolean]("Homozygote Count Finnish Indc")

    def alleleCountLatinoIndc = column[Boolean]("Allele Count Latino Indc")

    def alleleNumberLatinoIndc = column[Boolean]("Allele Number Latino Indc")

    def homozygoteCountLatinoIndc = column[Boolean]("Homozygote Count Latino Indc")

    def alleleCountOtherIndc = column[Boolean]("Allele Count Other Indc")

    def alleleNumberOtherIndc = column[Boolean]("Allele Number Other Indc")

    def homozygoteCountOtherIndc = column[Boolean]("Homozygote Count Other Indc")

    def alleleCountSouthAsianIndc = column[Boolean]("Allele Count South Asian Indc")

    def alleleNumberSouthAsianIndc = column[Boolean]("Allele Number South Asian Indc")

    def homozygoteCountSouthAsianIndc = column[Boolean]("Homozygote Count South Asian Indc")

    //def filterName = column[Option[String]]("Filter name", O.Default(Some("as")))

    def filterName = column[Option[String]]("Filter name" , O.Default(Some("as")))

    def id = column[Int]("User id", O.PrimaryKey)


    def * = (chromIndc :: positionIndc :: rsidIndc ::
      referenceIndc :: alternateIndc :: consequenceIndc ::
      proteinConsequenceIndc :: transcriptConsequenceIndc :: filterIndc ::
      annotationIndc :: flagsIndc :: alleleCountIndc ::
      alleleNumberIndc :: alleleFrequencyIndc :: numberOfHomozygotesIndc ::
      alleleCountAfricanIndc :: alleleNumberAfricanIndc :: homozygoteCountAfricanIndc ::
      alleleCountEastAsianIndc :: alleleNumberEastAsianIndc :: homozygoteCountEastAsianIndc ::
      alleleCountEuropeanNonFinnishIndc :: alleleNumberEuropeanNonFinnishIndc :: homozygoteCountEuropeanNonFinnishIndc ::
      alleleCountFinnishIndc :: alleleNumberFinnishIndc :: homozygoteCountFinnishIndc ::
      alleleCountLatinoIndc :: alleleNumberLatinoIndc :: homozygoteCountLatinoIndc ::
      alleleCountOtherIndc :: alleleNumberOtherIndc :: homozygoteCountOtherIndc ::
      alleleCountSouthAsianIndc :: alleleNumberSouthAsianIndc ::
      homozygoteCountSouthAsianIndc :: filterName :: id :: HNil).mappedWith(Generic[UserFilterSelectionModel])

  }

  var userFilters = TableQuery[UserFilterSelectionRepository]

  def getAll(): Future[List[UserFilterSelectionModel]] = db.run {
    val q = userFilters.filter(_.id === 1).result.headOption;
    println(q)

    userFilters.to[List].result
  }

  def getById(selectedId : Int) : Future[Option[UserFilterSelectionModel]] = db.run {
    userFilters.filter(_.id === selectedId).result.headOption
  }

}
