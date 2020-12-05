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
		if (gateway.existsByUsername(req.username))
			throw new UserAlreadyExistsException();

		User user = new User(req.username, req.email, req.password, UserRole.UNACTIVATED);
		user.validate();

		gateway.save(user);

		return new ResponseModel(String.format("User '%s' created.", req.username));
	}

	public static record RequestModel(String username, String password, String email) {
	}

	public static record ResponseModel(String message) {
	}

}
