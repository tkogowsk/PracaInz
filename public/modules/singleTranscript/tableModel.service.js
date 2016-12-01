angular.module('singleTranscript', []).service('TranscriptsTableModel', function () {
    var columnsList = [
        {
            headerName: "Filter Name",
            fieldName: "filterName",
        },
        {
            headerName: "id",
            fieldName: "id",
        },
        {
            headerName: "Chromosome",
            fieldName: "chrom",
        },
        {
            headerName: "Position",
            fieldName: "position",
        },
        {
            headerName: "RSID",
            fieldName: "rsid",
        },
        {
            headerName: "Reference",
            fieldName: "reference",
        },
        {
            headerName: "Alternate",
            fieldName: "alternate",
        },
        {
            headerName: "Consequence",
            fieldName: "consequence",
        },
        {
            headerName: "Protein Consequence",
            fieldName: "proteinConsequence",
        },
        {
            headerName: "Transcript Consequence",
            fieldName: "transcriptConsequence",
        },
        {
            headerName: "Annotation",
            fieldName: "annotation",
        },
        {
            headerName: "Flags",
            fieldName: "flags",
        },
        {
            headerName: "Allele Count",
            fieldName: "alleleCount",
        },
        {
            headerName: "Allele Number",
            fieldName: "alleleNumber",
        },
        {
            headerName: "Allele Frequency",
            fieldName: "alleleFrequency",
        },
        {
            headerName: "Number of Homozygotes",
            fieldName: "numberOfHomozygotes",
        },
        {
            headerName: "Allele Count African",
            fieldName: "alleleCountAfrican",
        },
        {
            headerName: "Allele Number African",
            fieldName: "alleleNumberAfrican",
        },
        {
            headerName: "Homozygote Count African",
            fieldName: "homozygoteCountAfrican",
        },
        {
            headerName: "Allele Count East Asian",
            fieldName: "alleleCountEastAsian",
        },
        {
            headerName: "Allele Number East Asian",
            fieldName: "alleleNumberEastAsian",
        },
        {
            headerName: "Homozygote Count East Asian",
            fieldName: "homozygoteCountEastAsian",
        },
        {
            headerName: "Allele Count European (Non-Finnish)",
            fieldName: "alleleCountEuropeanNonFinnish",
        },
        {
            headerName: "Allele Number European (Non-Finnish)",
            fieldName: "alleleNumberEuropeanNonFinnish",
        },
        {
            headerName: "Homozygote Count European (Non-Finnish)",
            fieldName: "homozygoteCountEuropeanNonFinnish",
        },
        {
            headerName: "Allele Count Finnish",
            fieldName: "alleleCountFinnish",
        },
        {
            headerName: "Allele Number Finnish",
            fieldName: "alleleNumberFinnish",
        },
        {
            headerName: "Homozygote Count Finnish",
            fieldName: "homozygoteCountFinnish",
        },
        {
            headerName: "Allele Count Latino",
            fieldName: "alleleCountLatino",
        },
        {
            headerName: "Allele Number Latino",
            fieldName: "alleleNumberLatino",
        },
        {
            headerName: "Homozygote Count Latino",
            fieldName: "homozygoteCountLatino",
        },
        {
            headerName: "Allele Count Other",
            fieldName: "alleleCountOther",
        },
        {
            headerName: "Allele Number Other",
            fieldName: "alleleNumberOther",
        },
        {
            headerName: "Homozygote Count Other",
            fieldName: "homozygoteCountOther",
        },
        {
            headerName: "Allele Count South Asian",
            fieldName: "alleleCountSouthAsian",
        },
        {
            headerName: "Allele Number South Asian",
            fieldName: "alleleNumberSouthAsian",
        },
        {
            headerName: "Homozygote Count South Asian",
            fieldName: "homozygoteCountSouthAsian",
        }
    ];
});