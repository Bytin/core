package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.NonNull;

public class UpdateSnippet extends AbstractSnippetInteractor
        implements Command<UpdateSnippet.RequestModel, UpdateSnippet.ResponseModel> {

    UserGateway userGateway;

    UpdateSnippet(SnippetGateway gateway, UserGateway userGateway) {
        super(gateway);
        this.userGateway = userGateway;
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        Snippet snippet = gateway.findById(request.snippet.id());
        if (!snippet.getOwner().getUsername().equals(request.snippet.owner().username()))
            throw new DifferentSnippetOwnerException(request.snippet.owner() + " doesn't own that snippet");

        gateway.save(buildNewFromOld(request.snippet));
        return new ResponseModel("Snippet has been successfully updated.");
    }

    private Snippet buildNewFromOld(SnippetDTO req) {
        return Snippet.builder().id(req.id()).title(req.title()).language(req.language()).framework(req.framework())
                .code(req.code()).description(req.description()).resource(req.resource()).hidden(req.hidden())
                .owner(userGateway.findByUserName(req.owner().username())).whenCreated(req.whenCreated())
                .whenLastModified(req.whenLastModified()).build();
    }

    public static record RequestModel(@NonNull SnippetDTO snippet) {
    }

    public static record ResponseModel(@NonNull String message) {
    }
}
