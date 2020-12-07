package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import core.usecase.UseCaseException.*;
import lombok.NonNull;

public class RetrievePublicSnippet extends AbstractSnippetInteractor
        implements Command<RetrievePublicSnippet.RequestModel, RetrievePublicSnippet.ResponseModel> {

    RetrievePublicSnippet(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel req) {
        if(!gateway.existsById(req.id))
            throw new NoSuchSnippetException(req.id);

        Snippet snippet = gateway.findById(req.id);

        if(snippet.isHidden())
            throw new HiddenSnippetException(snippet.getId());

        return new ResponseModel(snippet.toSnippetDto());
    }

    public static record RequestModel(long id) {
    }

    public static record ResponseModel(@NonNull SnippetDTO snippet) {
    }

}
