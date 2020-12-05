package core.gateway;

import core.entity.User;

public interface UserGateway extends Gateway<User, Long> {

	User findByUserName(String username);
	boolean existsByUsername(String username);
    
}
