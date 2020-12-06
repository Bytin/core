package core.usecase.snippet;

import java.time.LocalDateTime;

import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import lombok.Builder;
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
        Snippet snippet = gateway.findById(request.id);
        if (!snippet.getOwner().equals(request.owner))
            throw new DifferentSnippetOwnerException("Requester doesn't own that snippet");

        gateway.save(buildNewFromOld(request));
        return new ResponseModel("Snippet has been successfully updated.");
    }

    private Snippet buildNewFromOld(RequestModel req) {
        return Snippet.builder().id(req.id()).title(req.title()).language(req.language()).framework(req.framework())
                .code(req.code()).description(req.description()).resource(req.resource()).hidden(req.hidden())
                .owner(userGateway.findByUserName(req.owner())).whenCreated(req.whenCreated)
                .whenLastModified(req.whenLastModified).build();
    }

    @Builder
    public static record RequestModel(@NonNull Long id, @NonNull String title, @NonNull String language,
            String framework, @NonNull String code, @NonNull String description, String resource, @NonNull String owner,
            boolean hidden, @NonNull LocalDateTime whenCreated, @NonNull LocalDateTime whenLastModified) {
    }

    public static record ResponseModel(@NonNull String message) {
    }
}
