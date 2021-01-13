package core.boundary;

import core.usecase.user.CreateUser;
import core.usecase.user.RetrieveProfile;
import core.usecase.user.UpdateUserInfo;
import core.usecase.user.ActivateUser.ResponseModel;
import core.usecase.user.UpdateUserInfo.RequestModel;

public interface UserIOBoundary extends IOBoundary {
    CreateUser.ResponseModel createUser(CreateUser.RequestModel requestModel,
            CreateUser.Encoder encoder);

    RetrieveProfile.ResponseModel retrieveProfile(RetrieveProfile.RequestModel requestModel);

    UpdateUserInfo.ResponseModel updateUserInfo(RequestModel request);

    ResponseModel activateUser(core.usecase.user.ActivateUser.RequestModel activationRequest);
}
