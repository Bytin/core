package core.usecase.user;

import core.entity.User;
import core.gateway.UserGateway;
import core.usecase.UseCaseException.*;
import core.usecase.Command;

public class RetrieveProfile extends AbstractUserInteractor implements Command<RetrieveProfile.RequestModel, RetrieveProfile.ResponseModel> {

    RetrieveProfile(UserGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel req) {
        if(!gateway.existsByUsername(req.username))
            throw new NoSuchUserException(req.username);

        User user = gateway.findByUserName(req.username);

        return new ResponseModel(user.getId(), user.getUsername(), user.getEmail());
    }

    public static record RequestModel(String username) {
    }

    public static record ResponseModel(long id, String username, String email) {
    }

}
