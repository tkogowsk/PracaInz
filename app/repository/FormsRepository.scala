package repository

import javax.inject.Singleton

import models.FormsModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import shapeless._
import slickless._
import utils.{FormEditDTO, FormSaveDTO, NewFormModel}

@Singleton
class FormsRepository {
  private val db = Database.forConfig("postgresConf")

  var forms = TableQuery[FieldsTableRepository]
  var fields = TableQuery[FieldsTableRepository]


  class FieldsTableRepository(tag: Tag) extends Table[FormsModel](tag, "FORMS") {

    def id = column[Int]("ID", O.PrimaryKey)

    def value = column[String]("VALUE")

    def name = column[String]("NAME")

    def userId = column[Int]("USER_ID")

    def fieldFk = column[Int]("FIELD_FK")

    def field = foreignKey("FIELD_FK", fieldFk, fields)(_.id)

    def * = (id, value, name, userId, fieldFk) <> (FormsModel.tupled, FormsModel.unapply)

  }


  def getAll(): Future[List[FormsModel]] = db.run {
    forms.to[List].result
  }

  def getUserForm(filterName: String, userId: Int): Future[List[FormsModel]] = db.run {
    forms.filter(form => form.name === filterName && form.userId === userId).to[List].result
  }

  def getUserForms(userId: Int): Future[List[FormsModel]] = db.run {
    forms.filter(form => form.userId === userId).to[List].result
  }

  def save(fields: List[NewFormModel]): Future[List[FormsModel]] = db.run {
    fields.foreach(elem => println(elem))
    forms.to[List].result
  }

  def edit(formsList: List[FormsModel]): Future[List[FormsModel]] = {
    formsList.foreach(elem => {
      val updateAction = forms.insertOrUpdate(elem)
      db.run(updateAction)
    })
    db.run(forms.to[List].result)
  }

}
