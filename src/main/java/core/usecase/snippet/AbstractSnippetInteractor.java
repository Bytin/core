package core.usecase.snippet;

import java.util.Collection;
import java.util.List;
import core.gateway.SnippetGateway;

public abstract class AbstractSnippetInteractor {

        SnippetGateway gateway;

        AbstractSnippetInteractor(SnippetGateway gateway) {
                this.gateway = gateway;
        }

        // TODO should I just use Illegal state exceptions with custem messages?
        public static class HiddenSnippetException extends IllegalStateException {
                private static final long serialVersionUID = 1L;
        }

        public static class NoSuchSnippetException extends IllegalStateException {
                private static final long serialVersionUID = 1L;
        }

        public static class DifferentSnippetOwnerException extends IllegalStateException {
                public DifferentSnippetOwnerException(String string) {
                        super(string);
                }

                private static final long serialVersionUID = 1L;
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
