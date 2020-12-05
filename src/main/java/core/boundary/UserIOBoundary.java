package core.boundary;

import core.usecase.user.CreateUser;
import core.usecase.user.RetrieveProfile;
import core.usecase.user.UpdateUserInfo;
import core.usecase.user.UpdateUserInfo.RequestModel;

public interface UserIOBoundary extends IOBoundary{
    CreateUser.ResponseModel createUser(CreateUser.RequestModel requestModel);
    RetrieveProfile.ResponseModel retrieveUserProfile(RetrieveProfile.RequestModel requestModel);
	UpdateUserInfo.ResponseModel updateUserInfo(RequestModel request);

}
