package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc._
import utilities.JwtUtility

import scala.concurrent.Future

class DataSource {
  def getUser(email: String, userId: String): Option[UserInfo] =
    if (email == "test@example.com" && userId == "userId123") {
      Some(UserInfo(1, "John", "Nash", email))
    } else {
      None
    }
}

case class UserInfo(id: Int,
                    firstName: String,
                    lastName: String,
                    email: String)

case class User(email: String, userId: String)

case class UserRequest[A](userInfo: UserInfo, request: Request[A]) extends WrappedRequest(request)

class SecuredAuthenticator @Inject()(dataSource: DataSource) extends Controller {
  implicit val formatUserDetails = Json.format[User]

  object JWTAuthentication extends ActionBuilder[UserRequest] {
    def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      val jwtToken = request.headers.get("jw_token").getOrElse("")

      if (JwtUtility.isValidToken(jwtToken)) {
        JwtUtility.decodePayload(jwtToken).fold {
          Future.successful(Unauthorized("Invalid credential"))
        } { payload =>
          val userCredentials = Json.parse(payload).validate[User].get

          // Replace this block with data source
          val maybeUserInfo = dataSource.getUser(userCredentials.email, userCredentials.userId)

          maybeUserInfo.fold(Future.successful(Unauthorized("Invalid credential")))(userInfo => block(UserRequest(userInfo, request)))
        }
      } else {
        Future.successful(Unauthorized("Invalid credential"))
      }
    }
  }

}
