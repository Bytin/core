package core.mock;

import java.util.*;

import core.entity.User;
import core.entity.User.UserRole;
import core.gateway.UserGateway;

public class MockUserRepository implements UserGateway {

        private Map<Long, User> map = new HashMap<>() {
                private static final long serialVersionUID = 1L;

                {
                        put(1l, new User("noah", "sdlkfj", UserRole.USER));
                        put(2l, new User("kate", "sdlkfj", UserRole.USER));
                        put(3l, new User("andy", "sdlkfj", UserRole.USER));
                        put(4l, new User("alice", "sdlkfj", UserRole.USER));
                        put(5l, new User("adam", "sdlkfj", UserRole.USER));
                        put(5l, new User("james", "sdlkfj", UserRole.USER));
                }
        };

        @Override
        public Optional<User> findById(Long id) {
                return Optional.ofNullable(map.get(id));
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
        public Optional<User> findByUserName(String username) {
                return map.values().stream().filter(user -> user.getUsername().equals(username))
                                .findFirst();
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
                return findByUserName(username).isPresent();
        }

        @Override
        public void deleteById(Long id) {
                map.remove(id);
        }

}
