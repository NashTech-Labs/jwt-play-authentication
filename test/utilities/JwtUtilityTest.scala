package utilities

import org.scalatestplus.play.PlaySpec

class JwtUtilityTest extends PlaySpec {
  private val jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6" +
    "InRlc3RAZXhhbXBsZS5jb20iLCJ1c2VySWQiOiJ1c2VySWQxMjMifQ.mjMQN8m_wH1NSE9GGexCW_GUh8uruNco18jgt7AWuO4"

  val jwt = new JwtUtility

  "Jwt Utility" should {
    "be able to create token" in {
      val json = """{"email":"test@example.com","userId":"userId123"}"""

      val result = jwt.createToken(json)

      result mustEqual jwtToken
    }

    "validate the jwt token" in {
      val result = jwt.isValidToken(jwtToken)

      result mustEqual true
    }

    "not validate the jwt token" in {
      val result = jwt.isValidToken("jwtToken")

      result mustEqual false
    }

    "be able to decode payload from the jwt token" in {
      val response = """{"email":"test@example.com","userId":"userId123"}"""

      val result = jwt.decodePayload(jwtToken)

      result mustEqual Some(response)
    }
  }
}