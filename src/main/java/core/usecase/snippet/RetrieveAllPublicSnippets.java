package core.usecase.snippet;

import core.utils.Page;
import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public class RetrieveAllPublicSnippets extends AbstractSnippetInteractor implements
        Command<RetrieveAllPublicSnippets.RequestModel, RetrieveAllPublicSnippets.ResponseModel> {

    RetrieveAllPublicSnippets(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        Page<Snippet> page = gateway.findAllPublic(request.page, request.pageSize);
        return new ResponseModel(mapSnippetsToDtos(page));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestModel {
        int page, pageSize;
    }

    @Value
    public static class ResponseModel {
        Page<SnippetDTO> page;
    }

}
