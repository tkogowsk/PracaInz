package utils

import slick.driver.PostgresDriver
import slick.jdbc.PostgresProfile

import scala.tools.nsc.typechecker.Implicits

import com.github.tminglei.slickpg._

/*trait MyPostgresDriver extends PostgresDriver
  with PgArraySupport
  with PgHStoreSupport {

  override lazy val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  trait ImplicitsPlus extends Implicits
    with ArrayImplicits
    with HStoreImplicits

  trait SimpleQLPlus extends SimpleQL
    with ImplicitsPlus
}
*/

import com.github.tminglei.slickpg._

trait MyPostgresDriver extends ExPostgresDriver
  with PgArraySupport
  with PgDate2Support
  with PgNetSupport
  with PgLTreeSupport
  with PgRangeSupport
  with PgHStoreSupport
  with PgSearchSupport {

  val pgjson = "jsonb"
  ///
  override val api = new API with ArrayImplicits
    with DateTimeImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {}
}

object MyPostgresDriver extends MyPostgresDriver