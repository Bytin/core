package core.usecase.user;

import core.entity.ActivationToken;
import core.entity.User;
import core.entity.User.UserRole;
import core.gateway.ActivationTokenGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException;
import core.usecase.UseCaseException.NoSuchUserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import core.utils.ExpiringToken.TokenHasExpiredException;

public class ActivateUser extends AbstractUserInteractor
        implements Command<ActivateUser.RequestModel, ActivateUser.ResponseModel> {

    ActivationTokenGateway tokenGateway;

    ActivateUser(UserGateway gateway, ActivationTokenGateway tokenGateway) {
        super(gateway);
        this.tokenGateway = tokenGateway;
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        User user = gateway.findByUserName(request.username)
                .orElseThrow(() -> new NoSuchUserException(request.username));
        var token = validateToken(request);
        activateUser(user, token);
        return new ResponseModel("User '" + request.username + "' has been activated.");
    }

    ActivationToken validateToken(RequestModel req) {
        var token = tokenGateway.findByUsername(req.username)
                .orElseThrow(() -> new UseCaseException("A token is not assigned to " + req.username));
        if(!req.token.equals(token.toString()))
            throw new UseCaseException("That token is not the one assigned.");
        return token;
    }

    void activateUser(User user, ActivationToken token) {
        if (!token.isExpired())
            user.setRole(UserRole.USER);
        else
            throw new TokenHasExpiredException();
        tokenGateway.delete(token);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestModel {
        @NonNull
        String username, token;
    }

    @Value
    public static class ResponseModel {
        @NonNull
        String message;
    }

}
