package repository

import javax.inject.Inject
import play.api.db.{ Database, NamedDatabase }

class JDBCRepository @Inject()(@NamedDatabase("jdbcConf") db: Database) {

  def test = {
    var outString = "Number is "
    val conn = db.getConnection()
    println(conn)
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT 9 as testkey ")

      while (rs.next()) {
        outString += rs.getString("testkey")
      }
    } finally {
      conn.close()
    }
    println(outString)
  }
}

