package core.usecase.snippet;

import java.util.Collection;
import java.util.List;
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
                                .map(snip -> new SnippetDTO(snip)).collect(Collectors.toList()));
        }

        public static record RequestModel(int page, int pageSize) {
        }
        public static record ResponseModel(int numberOfSnippets, Collection<SnippetDTO> snippets) {
        }

        static <T> Collection<T> pageSection(Collection<T> collection, int page, int pageSize) {
                List<T> list = List.copyOf(collection);
                int offset = (page - 1) * pageSize;
                int end = Math.min(list.size(), offset + pageSize);
                if (end < offset)
                        return List.of();
                return list.subList(offset, end);
        }

}
