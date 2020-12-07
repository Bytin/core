package core.usecase.snippet;

import java.util.Collection;
import java.util.stream.Collectors;
import core.dto.SnippetDTO;
import core.gateway.SnippetGateway;
import core.usecase.Command;

public class RetrieveAllPublicSnippets extends AbstractSnippetInteractor implements
                Command<RetrieveAllPublicSnippets.RequestModel, RetrieveAllPublicSnippets.ResponseModel> {

        RetrieveAllPublicSnippets(SnippetGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                var snippets = gateway.findAll();
                var pageOfSnippets = pageSection(snippets, request.page, request.pageSize);

                return new ResponseModel(gateway.getSize(), pageOfSnippets.stream()
                                .map(snip -> snip.toSnippetDto()).collect(Collectors.toList()));
        }

        public static record RequestModel(int page, int pageSize) {
        }
        public static record ResponseModel(int numberOfSnippets, Collection<SnippetDTO> snippets) {
        }



}
