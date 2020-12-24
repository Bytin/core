package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.entity.User;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

public class CreateSnippet extends AbstractSnippetInteractor
                implements Command<CreateSnippet.RequestModel, CreateSnippet.ResponseModel> {

        UserGateway userGateway;

        CreateSnippet(SnippetGateway gateway, UserGateway userGateway) {
                super(gateway);
                this.userGateway = userGateway;
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                Snippet snippet = makeSnippetFromRequest(request.snippet);
                gateway.save(snippet);
                return new ResponseModel("Snippet has been successfully saved.");
        }

        private Snippet makeSnippetFromRequest(SnippetDTO req) {
                if (!userGateway.existsByUsername(req.getOwner().getUsername()))
                        throw new NoSuchUserException(req.getOwner().getUsername());

                User owner = userGateway.findByUserName(req.getOwner().getUsername()).orElseThrow(
                                () -> new NoSuchUserException(req.getOwner().getUsername()));
                Snippet snippet = new Snippet(req.getTitle(), req.getLanguage(), req.getCode(),
                                req.getDescription(), owner, req.getWhenCreated(),
                                req.getWhenLastModified());
                snippet.setHidden(req.isHidden());
                return snippet;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                private SnippetDTO snippet;
        }

        @Value
        public static class ResponseModel {
                private @NonNull String message;
        }
}
