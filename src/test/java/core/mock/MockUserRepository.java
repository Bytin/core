package core.mock;

import java.util.*;

import core.entity.User;
import core.entity.User.UserRole;
import core.gateway.UserGateway;

public class MockUserRepository implements UserGateway {

    private Map<Long, User> map = new HashMap<>() {
        private static final long serialVersionUID = 1L;

        {
            put(1l, new User(1, "noah", "noah@gmail.com", "sdlkfj", UserRole.USER));
            put(2l, new User(2, "kate", "kate@gmail.com", "sdlkfj", UserRole.USER));
            put(3l, new User(3, "andy", "andy@gmail.com", "sdlkfj", UserRole.USER));
            put(4l, new User(4, "alice", "alice@gmail.com", "sdlkfj", UserRole.USER));
            put(5l, new User(5, "adam", "adam@gmail.com", "sdlkfj", UserRole.USER));
            put(5l, new User(6, "james", "james@gmail.com", "sdlkfj", UserRole.USER));
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
        map.put(id, new User(id, user.getUsername(), user.getEmail(), user.getPassword(),
                user.getRole()));
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
