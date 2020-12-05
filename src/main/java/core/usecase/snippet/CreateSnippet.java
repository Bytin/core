package core.usecase.snippet;

import java.time.LocalDateTime;

import core.entity.Snippet;
import core.entity.User;
import core.gateway.SnippetGateway;
import core.gateway.UserGateway;
import core.usecase.Command;
import lombok.Builder;
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
        Snippet snippet = makeSnippetFromRequest(request);
        gateway.save(snippet);
        return new ResponseModel("Snippet has been successfully saved.");
    }

    @Override
    public void validate(RequestModel request) throws IllegalRequestModelException {
        //TODO test
    }

    private Snippet makeSnippetFromRequest(RequestModel req){
        User owner = userGateway.findByUserName(req.owner);
        Snippet snippet = new Snippet(req.title, req.language, req.code, req.description, owner, req.whenCreated, req.whenLastModified);
        snippet.setHidden(req.hidden);
        return snippet;
    }

    @Builder
    public static record RequestModel(@NonNull String title, @NonNull String language, String framework,
            @NonNull String code, @NonNull String description, String resource, @NonNull String owner, boolean hidden,
            @NonNull LocalDateTime whenCreated, @NonNull LocalDateTime whenLastModified) {
    }

    public static record ResponseModel(@NonNull String message) {
    }
}
