package core.entity;

import core.utils.ExpiringToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ActivationToken extends ExpiringToken {
    private final @Getter String username;

    public ActivationToken(String username, ExpiringToken token) {
        super(token);
        this.username = username;
    }
}
