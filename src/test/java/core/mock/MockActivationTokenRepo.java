package core.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import core.entity.ActivationToken;
import core.gateway.ActivationTokenGateway;
import lombok.NonNull;

public class MockActivationTokenRepo implements ActivationTokenGateway {

    Map<Long, ActivationToken> map = new HashMap<>();

    @Override
    public Optional<ActivationToken> findById(Long id) {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public void save(ActivationToken token) {
        map.put(token.getUuid().getMostSignificantBits(), token);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Collection<ActivationToken> findAll() {
        return map.values();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<ActivationToken> findByUser_username(@NonNull String username) {
        return map.values().stream().filter(token -> token.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void delete(ActivationToken token) {
        map.remove(token.getUuid().getMostSignificantBits());
    }

}
