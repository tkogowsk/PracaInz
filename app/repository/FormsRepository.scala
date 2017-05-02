package repository

import javax.inject.Singleton

import models.FormsModel
import slick.driver.PostgresDriver.api._

import scala.concurrent._
import utils.NewFormModel

@Singleton
@Deprecated
class FormsRepository {
  private val db = Database.forConfig("postgresConf")

  var forms = TableQuery[FormsTableRepository]

  class FormsTableRepository(tag: Tag) extends Table[FormsModel](tag, "FORMS") {

    def id = column[Int]("ID", O.PrimaryKey)

    def value = column[String]("VALUE")

    def name = column[String]("NAME")

    def userId = column[Int]("USER_ID")

    def fieldFk = column[Int]("FIELD_FK")

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

  def save(fields: List[NewFormModel]): Future[List[FormsModel]] =  {
    fields.foreach(elem => {
      db.run(sqlu"""insert into "FORMS"("NAME","VALUE","USER_ID","FIELD_FK") VALUES (${elem.name}, ${elem.value}, ${elem.userId}, ${elem.fieldFk})""")
    })
    db.run(forms.to[List].result)
  }

  def edit(formsList: List[FormsModel]): Future[List[FormsModel]] = {
    formsList.foreach(elem => {
      val updateAction = forms.insertOrUpdate(elem)
      db.run(updateAction)
    })
    db.run(forms.to[List].result)
  }

}
