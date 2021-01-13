package core.mock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import core.utils.ExpiringToken;

public class TestToken extends ExpiringToken{
    
    public TestToken(UUID uuid, long milliseconds){
        super(uuid, Duration.ofMillis(milliseconds), LocalDateTime.now());
    }
}
