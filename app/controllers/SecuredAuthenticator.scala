package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc._
import utilities.JwtUtility

import scala.concurrent.Future

case class User(email: String, userId: String)

case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest(request)

class SecuredAuthenticator @Inject() extends Controller {

  implicit val formatUserDetails = Json.format[User]

  object JWTAuthentication extends ActionBuilder[UserRequest] {
    def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      implicit val req = request

      val jwtToken = request.headers.get("jw_token").getOrElse("")

      if (JwtUtility.isValidToken(jwtToken)) {
        JwtUtility.decodePayload(jwtToken).fold(
          Future.successful(Unauthorized("Invalid credential"))
        ) { payload =>
          val userInfo = Json.parse(payload).validate[User].get

          // Replace this block with data source
          if (userInfo.email == "test@example.com" && userInfo.userId == "userId123") {
            Future.successful(Ok("Authorization successful"))
          } else {
            Future.successful(Unauthorized("Invalid credential"))
          }
        }
      }
      else {
        Future.successful(Unauthorized("Invalid credential"))
      }
    }
  }

}
object SecuredAuthenticator extends SecuredAuthenticator
