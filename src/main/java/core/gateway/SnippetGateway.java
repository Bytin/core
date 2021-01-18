package core.gateway;

import java.util.Collection;
import java.util.stream.Stream;
import core.utils.Page;
import core.entity.Snippet;

public interface SnippetGateway extends Gateway<Snippet, Long> {

  Collection<Snippet> findMostRecent(int size);

  Collection<Snippet> findAllByOwnerUsername(String owner);

  Page<Snippet> findAllPublic(int page, int pageSize);

  Page<Snippet> findAllByOwnerUsername(String owner, int page, int pageSize);

  Stream<Snippet> streamAll();

}
