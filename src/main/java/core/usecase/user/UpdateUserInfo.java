package core.usecase.user;

import core.entity.User;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
                User userWithNewInfo = new User(user.getId(), request.username, user.getPassword(), user.getRole());
                userWithNewInfo.validate();

                gateway.save(userWithNewInfo);

                return new ResponseModel("User info has been updated successfully.");
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                @NonNull
                String oldUsername, username;
        }

        @Value
        public static class ResponseModel {
                @NonNull
                String message;
        }

}
