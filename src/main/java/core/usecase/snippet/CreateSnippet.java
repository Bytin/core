package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.entity.User;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.NonNull;

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
                if (!userGateway.existsByUsername(req.owner().username()))
                        throw new NoSuchUserException(req.owner().username());

                User owner = userGateway.findByUserName(req.owner().username()).orElseThrow(() -> new NoSuchUserException(req.owner().username()));
                Snippet snippet = new Snippet(req.title(), req.language(), req.code(),
                                req.description(), owner, req.whenCreated(),
                                req.whenLastModified());
                snippet.setHidden(req.hidden());
                return snippet;
        }

        public static record RequestModel(@NonNull SnippetDTO snippet) {
        }

        public static record ResponseModel(@NonNull String message) {
        }
}
