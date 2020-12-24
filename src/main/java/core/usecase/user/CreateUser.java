package core.usecase.user;

import core.entity.User;
import core.entity.User.UserRole;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

                User user = new User(req.username, encoder.encode(req.password), UserRole.USER);
                user.validate();
                user.setEmail(req.email);
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
                String username, password, email;
        }

        @Value
        public static class ResponseModel {
                String message;
        }

}
