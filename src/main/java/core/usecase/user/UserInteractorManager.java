package core.usecase.user;

import core.boundary.UserIOBoundary;
import core.gateway.UserGateway;
import core.usecase.user.CreateUser.ResponseModel;
import core.usecase.user.RetrieveProfile.RequestModel;

public class UserInteractorManager extends AbstractUserInteractor implements UserIOBoundary {

	public UserInteractorManager(UserGateway gateway) {
		super(gateway);
	}

	@Override
	public ResponseModel createUser(core.usecase.user.CreateUser.RequestModel requestModel) {
		return new CreateUser(gateway).execute(requestModel);
	}

	@Override
	public core.usecase.user.RetrieveProfile.ResponseModel retrieveUserProfile(RequestModel requestModel) {
		return new RetrieveProfile(gateway).execute(requestModel);
	}

	@Override
	public UpdateUserInfo.ResponseModel updateUserInfo(core.usecase.user.UpdateUserInfo.RequestModel request) {
		return new UpdateUserInfo(gateway).execute(request);
	}

}
