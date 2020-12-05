package core.usecase.user;

import core.gateway.UserGateway;

public abstract class AbstractUserInteractor {
    
    UserGateway gateway;

    AbstractUserInteractor(UserGateway gateway){
        this.gateway = gateway;
    }

	public static class UserAlreadyExistsException extends IllegalStateException{
		private static final long serialVersionUID = 1L;
    }
    
	public static class NoSuchUserException extends IllegalStateException{
		private static final long serialVersionUID = 1L;
	}
    
}
