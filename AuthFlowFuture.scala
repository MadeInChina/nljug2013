




trait AuthFlows extends UserStore
                   with SessionStore
                   with ExecutionContextSupport {

  def login(data: LoginData): Future[LoginResponse] = {

    findUser(data.username).flatMap {
      case None       => successful(LoginFailed)
      case Some(user) => {
        if (!validatePassword(data.password, user.passwordHash)) {
          successful(LoginFailed)
        } else {
          createSession(user).map {
            case SessionCreated(session) => LoginSucceeded(session)
            case SessionNotCreated       => LoginFailed
          }
        }
      }
    }
  }
}









