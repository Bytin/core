package core.entity;

import core.utils.ExpiringToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ActivationToken extends ExpiringToken {
    protected @Getter String username;

    public ActivationToken(String username, ExpiringToken token) {
        super(token);
        this.username = username;
    }
}
