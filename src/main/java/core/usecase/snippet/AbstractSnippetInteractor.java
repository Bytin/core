package core.usecase.snippet;

import java.util.Collection;
import java.util.List;
import core.gateway.SnippetGateway;

public abstract class AbstractSnippetInteractor {

        SnippetGateway gateway;

        AbstractSnippetInteractor(SnippetGateway gateway) {
                this.gateway = gateway;
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
