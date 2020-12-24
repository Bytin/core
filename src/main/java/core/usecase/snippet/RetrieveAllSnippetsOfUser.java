package core.usecase.snippet;

import java.util.Collection;
import java.util.stream.Collectors;
import core.dto.SnippetDTO;
import core.dto.UserDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
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
                Collection<Snippet> snippets =
                                gateway.findAllByOwnerUsername(request.owner.getUsername());
                var snippetDtos = pageSection(snippets, request.page, request.pageSize).stream()
                                .map(snip -> snip.toSnippetDto()).collect(Collectors.toList());
                return new ResponseModel(snippets.size(), snippetDtos);
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                @NonNull
                UserDTO owner;
                int page, pageSize;
        }

        @Value
        public static class ResponseModel {
                int numberOfSnippets;
                Collection<SnippetDTO> snippets;
        }

}
