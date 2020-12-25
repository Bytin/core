package core.usecase.user;

import core.entity.User;
import core.entity.User.UserRole;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

public class CreateUser extends AbstractUserInteractor
                implements Command<CreateUser.RequestModel, CreateUser.ResponseModel> {

        CreateUser(UserGateway gateway) {
                super(gateway);
        }

        Encoder encoder;

        @Override
        public ResponseModel execute(RequestModel req) {
                if (encoder == null)
                        throw new NoEncoderRegisteredException();
                if (gateway.existsByUsername(req.username))
                        throw new UserAlreadyExistsException(req.username);

                User user = new User(0, req.username, encoder.encode(req.password), req.email, UserRole.USER);
                user.validate();
                gateway.save(user);

                return new ResponseModel(String.format("User '%s' created.", req.username));
        }

        @FunctionalInterface
        public interface Encoder {
                String encode(CharSequence chars);
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                @NonNull
                String username, password;
                String email;
        }

        @Value
        public static class ResponseModel {
                String message;
        }

}
