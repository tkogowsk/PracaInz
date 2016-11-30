package models


case class TranscriptModel
(
  chrom: Int, position: Int, rsid: String,
  reference: String, alternate: String, consequence: String,
  proteinConsequence: String, transcriptConsequence: String, filter: String,
  annotation: String, flags: String, alleleCount: Int,
  alleleNumber: Int, alleleFrequency: Double, numberOfHomozygotes: Double,
  alleleCountAfrican: Double, alleleNumberAfrican: Double, homozygoteCountAfrican: Double,
  alleleCountEastAsian: Double, alleleNumberEastAsian: Double, homozygoteCountEastAsian: Double,
  alleleCountEuropeanNonFinnish: Double, alleleNumberEuropeanNonFinnish: Double, homozygoteCountEuropeanNonFinnish: Double,
  alleleCountFinnish: Double, alleleNumberFinnish: Double, homozygoteCountFinnish: Double,
  alleleCountLatino: Double, alleleNumberLatino: Double, homozygoteCountLatino: Double,
  alleleCountOther: Double, alleleNumberOther: Double, homozygoteCountOther: Double,
  alleleCountSouthAsian: Double, alleleNumberSouthAsian: Double, homozygoteCountSouthAsian: Double
)

