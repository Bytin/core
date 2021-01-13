package core.utils;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import core.usecase.UseCaseException;
import lombok.Getter;
import lombok.NonNull;

public class ExpiringToken {

    @NonNull
    private final @Getter UUID uuid;

    @NonNull
    private final @Getter Duration lifeSpan;

    private final @Getter LocalDateTime dateCreated;

    public ExpiringToken() {
        this(10);
    }

    public ExpiringToken(long minutes) {
        this(Duration.ofMinutes(minutes));
    }

    public ExpiringToken(Duration duration) {
        lifeSpan = duration;
        uuid = UUID.randomUUID();
        dateCreated = LocalDateTime.now();
    }

    protected ExpiringToken(UUID uuid, Duration lifeSpan) {
        this.uuid = uuid;
        this.lifeSpan = lifeSpan;
        dateCreated = LocalDateTime.now();
    }

    protected ExpiringToken(ExpiringToken token) {
        this.uuid = token.uuid;
        this.lifeSpan = token.lifeSpan;
        this.dateCreated = token.dateCreated;
    }

    public boolean isExpired() {
        return dateCreated.plus(lifeSpan).isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    public static class TokenHasExpiredException extends UseCaseException{

        private static final long serialVersionUID = 1L;

        public TokenHasExpiredException() {
            super("That token has expired.");
        }
        
    }
}
