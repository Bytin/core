package core.usecase.user;

import core.dto.UserDTO;
import core.entity.User;
import core.gateway.UserGateway;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import core.usecase.Command;

public class RetrieveProfile extends AbstractUserInteractor
                implements Command<RetrieveProfile.RequestModel, RetrieveProfile.ResponseModel> {

        RetrieveProfile(UserGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel req) {

                User user = gateway.findByUserName(req.username)
                                .orElseThrow(() -> new NoSuchUserException(req.username));

                return new ResponseModel(user.toUserDto());
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                String username;
        }

        @Value
        public static class ResponseModel {
                UserDTO user;
        }

}
