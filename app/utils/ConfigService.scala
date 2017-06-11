package utils

import com.typesafe.config.ConfigFactory

object ConfigService {

  def getTranscriptTableSampleIdColumnName: String = {
    val value = ConfigFactory.load().getString("utils.transcriptTableSampleIdColumnName")
    value
  }

  def getTranscriptTableName: String = {
    val value = ConfigFactory.load().getString("utils.transcriptTableName")
    value
  }
}
