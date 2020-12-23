package core.gateway;

import java.util.Optional;
import core.entity.User;

public interface UserGateway extends Gateway<User, Long> {

        Optional<User> findByUserName(String username);

        boolean existsByUsername(String username);

}
