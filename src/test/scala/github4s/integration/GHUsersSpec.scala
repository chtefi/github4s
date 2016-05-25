package github4s.integration

import cats.Id
import cats.scalatest.{ XorMatchers, XorValues }
import github4s.Github._
import github4s.free.interpreters.IdInterpreters._
import github4s.Github
import github4s.utils.TestUtils
import org.scalatest._

class GHUsersSpec extends FlatSpec with Matchers with XorMatchers with XorValues with TestUtils {

  "Users >> Get" should "return the expected login for a valid username" in {
    val response = Github(accessToken).users.get(validUsername).exec[Id]
    response shouldBe right
    response.value.value.login shouldBe validUsername
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return error on Left for invalid username" in {
    val response = Github(accessToken).users.get(invalidUsername).exec[Id]
    response shouldBe left
  }

  "Users >> GetAuth" should "return error on Left when no accessToken is provided" in {
    val response = Github().users.getAuth.exec[Id]
    response shouldBe left
  }

  "Users >> GetUsers" should "return users for a valid since value" in {
    val response = Github(accessToken).users.getUsers(validSinceInt).exec[Id]
    response shouldBe right
    response.value.value.nonEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

  it should "return an empty list when a invalid since value is provided" in {
    val response = Github(accessToken).users.getUsers(invalidSinceInt).exec[Id]
    response shouldBe right
    response.value.value.isEmpty shouldBe true
    response.value.statusCode shouldBe okStatusCode
  }

}