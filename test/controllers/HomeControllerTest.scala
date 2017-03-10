package controllers

import play.api.test.{FakeRequest, PlaySpecification}

class HomeControllerTest extends PlaySpecification {

  def homeController: HomeController = {
    val dataSource = new DataSource
    val securedAuthenticator = new SecuredAuthenticator(dataSource)

    new HomeController(securedAuthenticator)
  }

  private val jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6" +
    "InRlc3RAZXhhbXBsZS5jb20iLCJ1c2VySWQiOiJ1c2VySWQxMjMifQ.mjMQN8m_wH1NSE9GGexCW_GUh8uruNco18jgt7AWuO4"

  "Home controller" should {
    "test index" in {
      val response = homeController.index(FakeRequest())

      status(of = response) must beEqualTo(UNAUTHORIZED)
    }

    "not test index" in {
      val response = homeController.index(FakeRequest().withHeaders("jw_token" -> jwtToken))

      status(of = response) must beEqualTo(OK)
    }
  }
}
