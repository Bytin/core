package core.usecase.user;

import core.boundary.UserIOBoundary;
import core.gateway.ActivationTokenGateway;
import core.gateway.UserGateway;
import core.usecase.user.CreateUser.ResponseModel;
import core.usecase.user.RetrieveProfile.RequestModel;

public class UserInteractorManager extends AbstractUserInteractor implements UserIOBoundary {

    ActivationTokenGateway tokenGateway;
	public UserInteractorManager(UserGateway gateway, ActivationTokenGateway tokenGateway) {
        super(gateway);
        this.tokenGateway = tokenGateway;
	}

	@Override
	public ResponseModel createUser(core.usecase.user.CreateUser.RequestModel requestModel, CreateUser.Encoder encoder) {
                var command = new CreateUser(gateway, tokenGateway);
                command.encoder = encoder;
                return command.execute(requestModel);
	}

	@Override
	public core.usecase.user.RetrieveProfile.ResponseModel retrieveUserProfile(RequestModel requestModel) {
		return new RetrieveProfile(gateway).execute(requestModel);
	}

	@Override
	public UpdateUserInfo.ResponseModel updateUserInfo(core.usecase.user.UpdateUserInfo.RequestModel request) {
		return new UpdateUserInfo(gateway).execute(request);
	}

    @Override
    public core.usecase.user.ActivateUser.ResponseModel activateUser(
            core.usecase.user.ActivateUser.RequestModel activationRequest) {
        return new ActivateUser(gateway, tokenGateway).execute(activationRequest);
    }

}
