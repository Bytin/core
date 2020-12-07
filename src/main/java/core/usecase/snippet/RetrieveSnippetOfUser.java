package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.NonNull;

public class RetrieveSnippetOfUser extends AbstractSnippetInteractor
        implements Command<RetrieveSnippetOfUser.RequestModel, RetrieveSnippetOfUser.ResponseModel> {

    RetrieveSnippetOfUser(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        Snippet snippet = gateway.findById(request.snippetId);

        if (!snippet.getOwner().getUsername().equals(request.username))
            throw new DifferentSnippetOwnerException(snippet.getOwner().getUsername());

        return new ResponseModel(new SnippetDTO(snippet));
    }

    public static record RequestModel(@NonNull Long snippetId, @NonNull String username) {
    }

    public static record ResponseModel(@NonNull SnippetDTO snippet){
    }

}
