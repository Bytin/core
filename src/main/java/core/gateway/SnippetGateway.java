package core.gateway;

import java.util.Collection;
import core.entity.Snippet;

public interface SnippetGateway extends Gateway<Snippet, Long> {

  Collection<Snippet> findMostRecent(int size);

  Collection<Snippet> findAllByOwnerUsername(String owner);

}
