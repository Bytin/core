package core.mock;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import core.utils.Page;
import core.entity.Snippet;
import core.gateway.SnippetGateway;

public class MockSnippetRepository implements SnippetGateway {

    Map<Long, Snippet> map = new HashMap<>();

    @Override
    public Optional<Snippet> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return map.containsKey(id);
    }

    @Override
    public void save(Snippet o) {
        long id = o.getId();
        if (id == 0)
            id = Math.max(1, map.size() + 1);
        o.setId(id);
        map.put(id, o);
    }

    @Override
    public Collection<Snippet> findAll() {
        return map.values();
    }

    @Override
    public Collection<Snippet> findMostRecent(int size) {
        return map.values().stream()
                .sorted((x, y) -> y.getWhenLastModified().compareTo(x.getWhenLastModified()))
                .limit(size).collect(Collectors.toList());
    }

    @Override
    public Collection<Snippet> findAllByOwnerUsername(String owner) {
        return map.values().stream().filter(snip -> snip.getOwner().getUsername().equals(owner))
                .collect(Collectors.toList());
    }

    public void clearRepo() {
        map.clear();
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }

    @Override
    public void delete(Snippet snippet) {
        map.remove(snippet.getId());
    }

    @Override
    public Page<Snippet> findAllPublic(int page, int pageSize) {
        var pubSnippets =
                map.values().stream().filter(s -> !s.isHidden()).collect(Collectors.toList());
        int totalPages = pageSize > 0 ? pubSnippets.size() / pageSize : pubSnippets.size();
        pubSnippets = paginate(page, pageSize, pubSnippets);
        return newPage(page, pageSize, pubSnippets, totalPages);
    }

    @Override
    public Page<Snippet> findAllByOwnerUsername(String owner, int page, int pageSize) {
        var hiddenSnippets = findAllByOwnerUsername(owner).stream().collect(Collectors.toList());
        int totalPages = pageSize > 0 ? hiddenSnippets.size() / pageSize : hiddenSnippets.size();
        hiddenSnippets = paginate(page, pageSize, hiddenSnippets);
        return newPage(page, pageSize, hiddenSnippets, totalPages);
    }

    private <T> List<T> paginate(int page, int pageSize, List<T> list) {
        int offset = (page - 1) * pageSize;
        int end = Math.min(list.size(), offset + pageSize);
        if (end < offset)
            list = List.of();
        else
            list = list.subList(offset, end);
        return list;
    }

    private <T> Page<T> newPage(int page, int pageSize, Collection<T> pubSnippets, int totalPages) {
        var pageOfSnippets = new Page<T>(page, totalPages, pageSize, pubSnippets);
        return pageOfSnippets;
    }

    @Override
    public void withSnippetsStream(Consumer<Stream<Snippet>> consumer) {
        consumer.accept(map.values().stream());
    }

    @Override
    public long count() {
        return map.size();
    }

}
