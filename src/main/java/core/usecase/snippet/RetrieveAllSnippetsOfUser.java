package core.usecase.snippet;

import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import core.utils.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

public class RetrieveAllSnippetsOfUser extends AbstractSnippetInteractor implements
        Command<RetrieveAllSnippetsOfUser.RequestModel, RetrieveAllSnippetsOfUser.ResponseModel> {

    RetrieveAllSnippetsOfUser(SnippetGateway gateway) {
        super(gateway);
    }

    @Override
    public ResponseModel execute(RequestModel request) {
        Page<Snippet> page = gateway.findAllByOwnerUsername(request.username, request.page, request.pageSize);
        return new ResponseModel(page.map(snip -> snip.toSnippetDto()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestModel {
        @NonNull
        String username;
        int page, pageSize;
    }

    @Value
    public static class ResponseModel {
        Page<SnippetDTO> page;
    }

}
