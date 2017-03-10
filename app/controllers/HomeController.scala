package controllers

import javax.inject._

import play.api.mvc._

@Singleton
class HomeController @Inject()(auth: SecuredAuthenticator) extends Controller {

  def index = auth.JWTAuthentication { implicit request =>
    Ok(views.html.index(s"Hello ${request.userInfo.firstName} ${request.userInfo.lastName}"))
  }
}
