package core.entity;

import core.utils.ExpiringToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ActivationToken extends ExpiringToken {
    private long id;
    private String username;

    public ActivationToken(long id, String username, ExpiringToken token) {
        super(token);
        this.username = username;
        this.id = id;
    }
}
