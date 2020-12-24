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

public class RetrieveRecentSnippets extends AbstractSnippetInteractor implements
                Command<RetrieveRecentSnippets.RequestModel, RetrieveRecentSnippets.ResponseModel> {

        RetrieveRecentSnippets(SnippetGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                var snippets = gateway.findMostRecent(request.size);
                return new ResponseModel(snippets.stream().map(snip -> snip.toSnippetDto())
                                .collect(Collectors.toList()));
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RequestModel {
                int size;
        }

        @Value
        public static class ResponseModel {
                Collection<SnippetDTO> snippets;
        }

}
