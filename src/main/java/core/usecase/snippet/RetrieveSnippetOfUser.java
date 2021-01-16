package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

public class RetrieveSnippetOfUser extends AbstractSnippetInteractor implements
                Command<RetrieveSnippetOfUser.RequestModel, RetrieveSnippetOfUser.ResponseModel> {

        RetrieveSnippetOfUser(SnippetGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                Snippet snippet = gateway.findById(request.snippetId)
                                .orElseThrow(() -> new NoSuchSnippetException(request.snippetId));

                if (!snippet.getOwner().getUsername().equals(request.username))
                        throw new DifferentSnippetOwnerException(request.username);

                return new ResponseModel(snippet.toSnippetDto());
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                @NonNull
                Long snippetId;
                @NonNull
                String username;
        }

        @Value
        public static class ResponseModel {
                @NonNull
                SnippetDTO snippet;
        }

}
