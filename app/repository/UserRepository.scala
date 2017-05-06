package repository

import javax.inject.Singleton

import models.UserModel
import utils.AuthenticationDTO
import utils.MyPostgresDriver.api._

import scala.concurrent.Future

@Singleton
class UserRepository {

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

}
