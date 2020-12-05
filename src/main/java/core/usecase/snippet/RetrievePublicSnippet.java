package core.usecase.snippet;

import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import lombok.Builder;
import lombok.NonNull;

public class RetrievePublicSnippet extends AbstractSnippetInteractor
        implements Command<RetrievePublicSnippet.RequestModel, RetrievePublicSnippet.ResponseModel> {

    RetrievePublicSnippet(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel req) {
        Snippet snippet = gateway.findById(req.id);

        if(snippet.isHidden())
            throw new HiddenSnippetException();

        return ResponseModel.builder().title(snippet.getTitle()).language(snippet.getLanguage())
                .framework(snippet.getFramework()).code(snippet.getCode()).description(snippet.getDescription())
                .resource(snippet.getResource()).owner(snippet.getOwner()).whenCreated("2020-12-05 00:00")
                .hidden(snippet.isHidden())
                .whenLastModified("2020-12-05 00:00").build();
    }

    @Override
    public void validate(RequestModel req) throws IllegalRequestModelException {
        // TODO test
    }

    public static record RequestModel(long id) {
    }

    @Builder
    public static record ResponseModel(@NonNull String title, @NonNull String language, String framework,
            @NonNull String code, @NonNull String description, String resource, @NonNull String owner, boolean hidden,
            @NonNull String whenCreated, @NonNull String whenLastModified) {
    }

}
