package repository

import javax.inject.{Inject, Singleton}

import models._
import slick.driver.PostgresDriver.api._

import scala.concurrent._

@Singleton
class TestRepository {
  private val db = Database.forConfig("postgresConf")

/*
  var forms = TableQuery[FormsRepository]
  var fields = TableQuery[FieldsRepository]

  def getAllForms() = db.run {
    //forms.to[List].result && _.userId === 1
    forms.filter(form => form.name === "Second" && form.userId === 1).to[List].result
  }

  def getAllFields(): Future[List[FieldsModel]] = db.run {
    fields.to[List].result
  }
*/


}
