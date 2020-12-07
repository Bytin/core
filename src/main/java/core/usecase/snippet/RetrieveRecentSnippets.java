package core.usecase.snippet;

import java.util.Collection;
import java.util.stream.Collectors;
import core.dto.SnippetDTO;
import core.gateway.SnippetGateway;
import core.usecase.Command;

public class RetrieveRecentSnippets extends AbstractSnippetInteractor implements Command<RetrieveRecentSnippets.RequestModel, RetrieveRecentSnippets.ResponseModel> {

        RetrieveRecentSnippets(SnippetGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                var snippets = gateway.findMostRecent(request.size);
                return new ResponseModel(snippets.stream().map(snip -> snip.toSnippetDto()).collect(Collectors.toList()));
        }

        public static record RequestModel(int size) {
        }

        public static record ResponseModel(Collection<SnippetDTO> snippets) {
        }

}
