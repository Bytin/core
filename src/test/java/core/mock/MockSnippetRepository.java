package core.mock;

import java.util.*;

import core.entity.Snippet;
import core.gateway.SnippetGateway;

public class MockSnippetRepository implements SnippetGateway {

        Map<Long, Snippet> map = new HashMap<>();

        @Override
        public Snippet findById(Long id) {
                return map.get(id);
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
        public int getSize() {
                return map.size();
        }

        @Override
        public Collection<Snippet> findAll() {
                return map.values();
        }

}
