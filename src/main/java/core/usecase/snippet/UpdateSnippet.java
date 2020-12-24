package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.entity.User;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.NonNull;
import lombok.Value;

public class UpdateSnippet extends AbstractSnippetInteractor
                implements Command<UpdateSnippet.RequestModel, UpdateSnippet.ResponseModel> {

        UserGateway userGateway;

        UpdateSnippet(SnippetGateway gateway, UserGateway userGateway) {
                super(gateway);
                this.userGateway = userGateway;
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                Snippet snippet = gateway.findById(request.snippet.getId()).orElseThrow(
                                () -> new NoSuchSnippetException(request.snippet.getId()));
                if (!snippet.getOwner().getUsername().equals(request.snippet.getOwner().getUsername()))
                        throw new DifferentSnippetOwnerException(
                                        request.snippet.getOwner().getUsername());

                gateway.save(buildNewFromOld(request.snippet));
                return new ResponseModel("Snippet has been successfully updated.");
        }

        private Snippet buildNewFromOld(SnippetDTO req) {
                User owner = userGateway.findByUserName(req.getOwner().getUsername())
                                .orElseThrow(() -> new NoSuchUserException(req.getOwner().getUsername()));
                return Snippet.builder().id(req.getId()).title(req.getTitle()).language(req.getLanguage())
                                .framework(req.getFramework()).code(req.getCode())
                                .description(req.getDescription()).resource(req.getResource())
                                .hidden(req.isHidden()).owner(owner).whenCreated(req.getWhenCreated())
                                .whenLastModified(req.getWhenLastModified()).build();
        }

        @Value
        public static class RequestModel {
                @NonNull
                SnippetDTO snippet;
        }

        @Value
        public static class ResponseModel {
                @NonNull
                String message;
        }
}
