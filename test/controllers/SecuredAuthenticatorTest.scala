package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import models.DataSource
import play.api.mvc.Results
import play.api.test.{FakeRequest, PlaySpecification}

class SecuredAuthenticatorTest extends PlaySpecification {

  def securedAuth: SecuredAuthenticator = {
    val dataSource = new DataSource

    new SecuredAuthenticator(dataSource)
  }

  private val jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJ1c2VySWQiOiJ1c2VySWQxMjMifQ.mjMQN8m_wH1NSE9GGexCW_GUh8uruNco18jgt7AWuO4"

  private val jwtToken1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAZXhh" +
    "bXBsZS5jb20iLCJ1c2VySWQiOiIxMjM0NSJ9.jdkbiAZ1oev-LJrMGXqEduWjb612H8_x-hkKHnK-Z8s"

  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()

  "Secured Authenticator" should {

    "validate the request with validate jwt token" in {
      val response = call(securedAuth.JWTAuthentication { request =>
        Results.Ok("test jwt secured authenticator")
      }, FakeRequest().withHeaders("jw_token" -> jwtToken))

      status(of = response) must beEqualTo(OK)
      contentAsString(of = response) must beEqualTo("test jwt secured authenticator")
    }

    "not validate the request when user details does not match" in {
      val response = call(securedAuth.JWTAuthentication { request =>
        Results.Ok("test jwt secured authenticator")
      }, FakeRequest().withHeaders("jw_token" -> jwtToken1))

      status(of = response) must beEqualTo(UNAUTHORIZED)
      contentAsString(of = response) must beEqualTo("Invalid credential")
    }

    "not validate the request when jwt token is not a valid token" in {
      val response = call(securedAuth.JWTAuthentication { request =>
        Results.Ok("test jwt secured authenticator")
      }, FakeRequest().withHeaders("jw_token" -> "invalidjwtToken"))

      status(of = response) must beEqualTo(UNAUTHORIZED)
      contentAsString(of = response) must beEqualTo("Invalid credential")
    }

    "not validate the request when jwt not found" in {
      val response = call(securedAuth.JWTAuthentication { request =>
        Results.Ok("test jwt secured authenticator")
      }, FakeRequest())

      status(of = response) must beEqualTo(UNAUTHORIZED)
      contentAsString(of = response) must beEqualTo("Invalid credential")
    }

  }

}
