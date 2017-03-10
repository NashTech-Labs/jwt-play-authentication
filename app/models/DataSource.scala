package models

import controllers.UserInfo

class DataSource {
  def getUser(email: String, userId: String): Option[UserInfo] =
    if (email == "test@example.com" && userId == "userId123") {
      Some(UserInfo(1, "John", "Nash", email))
    } else {
      None
    }
}
