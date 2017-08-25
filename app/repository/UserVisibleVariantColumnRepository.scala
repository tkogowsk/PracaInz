package repository

import models.UserVisibleVariantColumnModel
import utils.MyPostgresDriver.api._
import utils.dtos.VisibleColumnDTO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration

object UserVisibleVariantColumnRepository {

  private val db = Database.forConfig("postgresConf")

  var visibleColumns = TableQuery[UserVisibleVariantColumnTableRepository]

  class UserVisibleVariantColumnTableRepository(tag: Tag) extends Table[UserVisibleVariantColumnModel](tag, "user_visible_variant_column") {

    def userId = column[Int]("user_id")

    def variantColumnId = column[Int]("variant_column_id")

    def * = (userId, variantColumnId) <> (UserVisibleVariantColumnModel.tupled, UserVisibleVariantColumnModel.unapply)

  }

  def save(userId: Int, visibleColumns: List[VisibleColumnDTO]) = {
    Await.result({
      db.run(
        sqlu"""DELETE from "user_visible_variant_column" Where "user_id" = ${userId}""")
    }, Duration.Inf)

    visibleColumns.foreach(elem => {
      Await.result({
        db.run(
          sqlu"""INSERT INTO "user_visible_variant_column"("user_id", "variant_column_id")
              VALUES (${userId}, ${elem.id})""")
      }, Duration.Inf)
    })
  }

  def get(userId: Int): Future[List[UserVisibleVariantColumnModel]] = {
    db.run {
      visibleColumns.filter(_.userId === userId).to[List].result
    }
  }

}
