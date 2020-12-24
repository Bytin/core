package core.usecase.user;

import core.entity.User;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.NonNull;
import lombok.Value;

public class UpdateUserInfo extends AbstractUserInteractor
                implements Command<UpdateUserInfo.RequestModel, UpdateUserInfo.ResponseModel> {

        UpdateUserInfo(UserGateway gateway) {
                super(gateway);
        }

        public ResponseModel execute(RequestModel request) {
                if (gateway.existsByUsername(request.username))
                        throw new UserAlreadyExistsException(request.username);

                User user = gateway.findByUserName(request.oldUsername)
                                .orElseThrow(() -> new NoSuchUserException(request.oldUsername));

                user.setUsername(request.username);
                user.setEmail(request.email);

                gateway.save(user);

                return new ResponseModel("User info has been updated successfully.");
        }

        @Value
        public static class RequestModel {
                @NonNull
                String oldUsername, username, email;
        }

        @Value
        public static class ResponseModel {
                @NonNull
                String message;
        }

}
