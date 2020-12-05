package core.usecase.user;

import core.entity.User;
import core.gateway.UserGateway;
import core.usecase.Command;
import lombok.NonNull;

public class UpdateUserInfo extends AbstractUserInteractor
        implements Command<UpdateUserInfo.RequestModel, UpdateUserInfo.ResponseModel> {

    UpdateUserInfo(UserGateway gateway) {
        super(gateway);
    }

	public ResponseModel execute(RequestModel request) {
        validate(request);

        User user = gateway.findByUserName(request.oldUsername);

        if(gateway.existsByUsername(request.username))
            throw new UserAlreadyExistsException();

        user.setUsername(request.username);
        user.setEmail(request.email);

        gateway.save(user);

		return new ResponseModel("User info has been updated successfully.");
	}

    @Override
    public void validate(RequestModel req) throws IllegalRequestModelException {
        //TODO write test for this
    }

    public static record RequestModel(@NonNull String oldUsername, @NonNull String username, @NonNull String email) {
    }

	public static record ResponseModel(@NonNull String message) {
	}

}
