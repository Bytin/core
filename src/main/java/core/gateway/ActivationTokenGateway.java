package core.gateway;

import java.util.Optional;
import core.entity.ActivationToken;
import lombok.NonNull;

public interface ActivationTokenGateway extends Gateway<ActivationToken, Long> {

    Optional<ActivationToken> findByUser_username(@NonNull String username);

}
