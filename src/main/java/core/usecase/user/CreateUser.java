package core.usecase.user;

import core.entity.User;
import core.entity.User.UserRole;
import core.gateway.UserGateway;
import core.usecase.Command;

public class CreateUser extends AbstractUserInteractor
		implements Command<CreateUser.RequestModel, CreateUser.ResponseModel> {

	CreateUser(UserGateway gateway) {
		super(gateway);
	}

	@Override
	public ResponseModel execute(RequestModel req) {
		validate(req);

		if(gateway.existsByUsername(req.username))
			throw new UserAlreadyExistsException();

		gateway.save(new User(req.username, req.email, req.password, UserRole.UNACTIVATED));

		return new ResponseModel(String.format("User '%s' created.", req.username));
	}

	@Override
	public void validate(RequestModel req) throws IllegalRequestModelException {
		validateRequestExceptWhenAnyNull(() -> {
			if (req.username.isBlank())
				throw new IllegalRequestModelException("Bad Username: should not be blank");
			if (req.email.isBlank())
				throw new IllegalRequestModelException("Bad email: should not be blank");
			if (req.password.isBlank())
				throw new IllegalRequestModelException("Bad Password: should not be blank");
			if (req.password.length() < 6)
				throw new IllegalRequestModelException("Bad Password: should be at least six characters long");
		});
		// TODO check email and password patterns
	}

	public static record RequestModel(String username, String password, String email) {
	}

	public static record ResponseModel(String message) {
	}
	

}
