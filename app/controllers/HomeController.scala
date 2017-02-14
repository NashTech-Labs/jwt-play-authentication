package controllers

import javax.inject._

import play.api.mvc._
import controllers.SecuredAuthenticator._

@Singleton
class HomeController @Inject() extends Controller {

  def index = JWTAuthentication { implicit request =>
    Ok(views.html.index("Hello world"))
  }
}
