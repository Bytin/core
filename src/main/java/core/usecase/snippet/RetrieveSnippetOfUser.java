package core.usecase.snippet;

import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import lombok.Builder;
import lombok.NonNull;

public class RetrieveSnippetOfUser extends AbstractSnippetInteractor
        implements Command<RetrieveSnippetOfUser.RequestModel, RetrieveSnippetOfUser.ResponseModel> {

    RetrieveSnippetOfUser(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        Snippet snippet = gateway.findById(request.snippetId);

        if (!snippet.getOwner().equals(request.username))
            throw new DifferentSnippetOwnerException("Requester doesn't own that snippet");

        return new ResponseModel(snippet);
    }

    public static record RequestModel(@NonNull Long snippetId, @NonNull String username) {
    }

    @Builder
    public static record ResponseModel(@NonNull String title, @NonNull String language, String framework,
            @NonNull String code, @NonNull String description, String resource, @NonNull String owner, boolean hidden,
            @NonNull String whenCreated, @NonNull String whenLastModified) {

        public ResponseModel(Snippet snip) {
            this(snip.getTitle(), snip.getLanguage(), snip.getFramework(), snip.getCode(), snip.getDescription(),
                    snip.getResource(), snip.getOwner(), snip.isHidden(), snip.getWhenCreated(),
                    snip.getWhenLastModified());
        }
    }

}
