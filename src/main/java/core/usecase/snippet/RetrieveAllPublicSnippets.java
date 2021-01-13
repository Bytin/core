package core.usecase.snippet;

import java.util.Collection;
import java.util.stream.Collectors;
import core.dto.SnippetDTO;
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
                var snippets = gateway.findAll();
                var pageOfSnippets = pageSection(snippets, request.page, request.pageSize);

                return new ResponseModel(snippets.size(), pageOfSnippets.stream()
                                .map(snip -> snip.toSnippetDto()).collect(Collectors.toList()));
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                int page, pageSize;
        }
        @Value
        public static class ResponseModel {
                int numberOfSnippets;
                Collection<SnippetDTO> snippets;
        }



}
