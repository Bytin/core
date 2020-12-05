package core.usecase.user;

import core.entity.User;
import core.gateway.UserGateway;
import core.usecase.Command;

public class RetrieveProfile extends AbstractUserInteractor implements Command<RetrieveProfile.RequestModel, RetrieveProfile.ResponseModel> {

    RetrieveProfile(UserGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel req) {
        validate(req);

        if(!gateway.existsByUsername(req.username))
            throw new NoSuchUserException();

        User user = gateway.findByUserName(req.username);

        return new ResponseModel(user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public void validate(RequestModel req) throws IllegalRequestModelException {
        validateRequestExceptWhenAnyNull(() -> {
            if (req.username.isBlank())
                throw new IllegalRequestModelException("Bad username: shouldn't be blank");
        });
    }

    public static record RequestModel(String username) {
    }

    public static record ResponseModel(long id, String username, String email) {
    }

}
