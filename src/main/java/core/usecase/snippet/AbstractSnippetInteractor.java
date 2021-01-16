package core.usecase.snippet;

import java.util.stream.Collectors;
import core.dto.SnippetDTO;
import core.entity.Snippet;
import core.gateway.SnippetGateway;
import core.utils.Page;

public abstract class AbstractSnippetInteractor {

    SnippetGateway gateway;

    AbstractSnippetInteractor(SnippetGateway gateway) {
        this.gateway = gateway;
    }

    static Page<SnippetDTO> mapSnippetsToDtos(Page<Snippet> page) {
        var response = new Page<SnippetDTO>();
        response.setNumber(page.getNumber());
        response.setSize(page.getSize());
        response.setTotal(page.getTotal());
        response.setContent(page.getContent().stream().map(snip -> snip.toSnippetDto())
                .collect(Collectors.toList()));
        return response;
    }

}
