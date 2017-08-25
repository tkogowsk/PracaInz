package repository

import models.UserModel
import utils.MyPostgresDriver.api._
import utils.dtos.AuthenticationDTO
import com.roundeights.hasher.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object UserRepository {

  private val db = Database.forConfig("postgresConf")
  var userTable = TableQuery[UserTableRepository]

  class UserTableRepository(tag: Tag) extends Table[UserModel](tag, "user") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[String]("name")

    def role = column[String]("role")

    def salt = column[String]("salt")

    def hash = column[String]("hash")

    def * = (id, name, role, salt, hash) <> (UserModel.tupled, UserModel.unapply)

  }

  def authenticate(dto: AuthenticationDTO): Option[UserModel] = {
    var user = None: Option[UserModel]
    Await.result(
      db.run {
        userTable.filter(_.name === dto.name).result.headOption.map { elem =>
          if (elem.isDefined) {
            user = Option(elem.get)
            val salt = user.get.salt
            val password = dto.password
            val hash = password.salt(salt).sha512.hex
            if(hash != user.get.hash) {
              throw new IllegalAccessException()
            }
          }
        }
      }, Duration.Inf)
    user
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

  def getIfAdmin(name: String): Option[UserModel] = {
    var user = None: Option[UserModel]
    Await.result({
      db.run {
        userTable.filter(_.name === name).result.headOption
      }.map { value =>
        if (value.isDefined && value.get.role == "admin") {
          user = value
        }
      }
    }, Duration.Inf)
    user
  }

  def register(dto: AuthenticationDTO) {
    val random = scala.util.Random
    val salt = random.nextInt.toString
    val password = dto.password
    val hash = password.salt(salt).sha512.hex
    Await.result({
      db.run {
        sqlu"""INSERT INTO "user"("name", "hash", "salt")
              VALUES (${dto.name}, ${hash}, ${salt})"""
      }
    }, Duration.Inf)
  }

}
