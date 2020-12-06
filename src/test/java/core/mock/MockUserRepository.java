package core.mock;

import java.util.*;

import core.entity.User;
import core.gateway.UserGateway;

public class MockUserRepository implements UserGateway {

        private Map<Long, User> map = new HashMap<>();

        @Override
        public User findById(Long id) {
                return map.get(id);
        }

        @Override
        public void save(User user) {
                long id = user.getId();
                if (id == 0)
                        id = Math.max(1, map.size() + 1);
                user.setId(id);
                map.put(id, user);
        }

        @Override
        public Collection<User> findAll() {
                return map.values();
        }

        @Override
        public User findByUserName(String username) {
                return map.values().stream().filter(user -> user.getUsername().equals(username))
                                .findFirst().orElse(null);
        }

        @Override
        public boolean existsById(Long id) {
                return map.containsKey(id);
        }

        @Override
        public int getSize() {
                return map.size();
        }

        @Override
        public boolean existsByUsername(String username) {
                return findByUserName(username) != null;
        }

}
