package core.usecase.snippet;

import core.gateway.SnippetGateway;
import core.usecase.Command;

public class DeleteSnippetOfUser extends AbstractSnippetInteractor implements
                Command<DeleteSnippetOfUser.RequestModel, DeleteSnippetOfUser.ResponseModel> {

        DeleteSnippetOfUser(SnippetGateway gateway) {
                super(gateway);
        }

        public static record RequestModel(long snippetId) {
        }

        public static record ResponseModel(String message) {
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                gateway.deleteById(request.snippetId);
                return new ResponseModel("Snippet was deleted successfully.");
        }
}
