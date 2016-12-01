package models


case class UserFilterSelectionModel
(
    chromIndc: Boolean, positionIndc: Boolean, rsidIndc: Boolean,
    referenceIndc: Boolean, alternateIndc: Boolean, consequenceIndc: Boolean,
    proteinConsequenceIndc: Boolean, transcriptConsequenceIndc: Boolean, filterIndc: Boolean,
    annotationIndc: Boolean, flagsIndc: Boolean, alleleCountIndc: Boolean,
    alleleNumbeIndcr: Boolean, alleleFrequencyIndc: Boolean, numberOfHomozygotesIndc: Boolean,
    alleleCountAfricanIndc: Boolean, alleleNumberAfricanIndc: Boolean, homozygoteCountAfricanIndc: Boolean,
    alleleCountEastAsianIndc: Boolean, alleleNumberEastAsianIndc: Boolean, homozygoteCountEastAsianIndc: Boolean,
    alleleCountEuropeanNonFinnishIndc: Boolean, alleleNumberEuropeanNonFinnishIndc: Boolean, homozygoteCountEuropeanNonFinnishIndc: Boolean,
    alleleCountFinnishIndc: Boolean, alleleNumberFinnishIndc: Boolean, homozygoteCountFinnishIndc: Boolean,
    alleleCountLatinoIndc: Boolean, alleleNumberLatinoIndc: Boolean, homozygoteCountLatinoIndc: Boolean,
    alleleCountOtherIndc: Boolean, alleleNumberOtherIndc: Boolean, homozygoteCountOtherIndc: Boolean,
    alleleCountSouthAsianIndc: Boolean, alleleNumberSouthAsianIndc: Boolean,
    homozygoteCountSouthAsianIndc: Boolean, filterName: Option[String], id :Int

)
