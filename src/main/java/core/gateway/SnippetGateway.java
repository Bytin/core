package core.gateway;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;
import core.utils.Page;
import core.entity.Snippet;

public interface SnippetGateway extends Gateway<Snippet, Long> {

  Collection<Snippet> findMostRecent(int size);

  Collection<Snippet> findAllByOwnerUsername(String owner);

  Page<Snippet> findAllPublic(int page, int pageSize);

  Page<Snippet> findAllByOwnerUsername(String owner, int page, int pageSize);

  void withPublicSnippetsStream(Consumer<Stream<Snippet>> consumer);

}
