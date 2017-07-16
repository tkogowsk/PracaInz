package repository

import models.UserModel
import utils.MyPostgresDriver.api._
import utils.dtos.AuthenticationDTO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object UserRepository {

  private val db = Database.forConfig("postgresConf")
  var userTable = TableQuery[UserTableRepository]

  class UserTableRepository(tag: Tag) extends Table[UserModel](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[String]("name")

    def * = (id, name) <> (UserModel.tupled, UserModel.unapply)

  }

  def authenticate(dto: AuthenticationDTO): Future[Option[UserModel]] = db.run {
    userTable.filter(_.name === dto.name).result.headOption
  }

  def getAll(): Future[List[UserModel]] = db.run {
    userTable.to[List].result
  }

  def getByName(name: String): Option[UserModel] = {
    var user = None: Option[UserModel]
    Await.result({
      db.run {
        userTable.filter(_.name === name).result.headOption
      }.map { value =>
        if (value.isDefined) {
          user = Option(value.get)
        }
      }
    }, Duration.Inf)
    user
  }

}
