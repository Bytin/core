package core.usecase.snippet;

import java.util.Collection;
import java.util.stream.Collectors;
import core.dto.SnippetDTO;
import core.dto.UserDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.usecase.Command;
import lombok.NonNull;

public class RetrieveAllSnippetsOfUser extends AbstractSnippetInteractor implements
                Command<RetrieveAllSnippetsOfUser.RequestModel, RetrieveAllSnippetsOfUser.ResponseModel> {

        RetrieveAllSnippetsOfUser(SnippetGateway gateway) {
                super(gateway);
        }

        @Override
        public ResponseModel execute(RequestModel request) {
                Collection<Snippet> snippets = gateway.findAllByOwnerUsername(request.owner.username());
                var snippetDtos = pageSection(snippets, request.page, request.pageSize).stream()
                                .map(snip -> new SnippetDTO(snip)).collect(Collectors.toList());
                return new ResponseModel(snippets.size(), snippetDtos);
        }

        public static record RequestModel(@NonNull UserDTO owner, int page, int pageSize) {
        }

        public static record ResponseModel(int numberOfSnippets, Collection<SnippetDTO> snippets) {
        }

}
