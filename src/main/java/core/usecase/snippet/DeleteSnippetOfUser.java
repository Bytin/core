package core.usecase.snippet;

import core.gateway.SnippetGateway;
import core.usecase.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public class DeleteSnippetOfUser extends AbstractSnippetInteractor implements
                Command<DeleteSnippetOfUser.RequestModel, DeleteSnippetOfUser.ResponseModel> {

        DeleteSnippetOfUser(SnippetGateway gateway) {
                super(gateway);
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                private long snippetId;
        }

        @Value
        public static class ResponseModel {
                String message;
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                gateway.deleteById(request.snippetId);
                return new ResponseModel("Snippet was deleted successfully.");
        }
}
