package core.utils;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import core.usecase.UseCaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class ExpiringToken {

    public static final long DEFAULT_LIFESPAN_MINUTES = 10;

    @NonNull
    private final @Getter UUID uuid;

    @NonNull
    private final @Getter Duration lifeSpan;

    private final @Getter LocalDateTime dateCreated;

    public ExpiringToken() {
        this(Duration.ofMinutes(DEFAULT_LIFESPAN_MINUTES));
    }

    public ExpiringToken(Duration duration) {
        this(UUID.randomUUID(), duration, LocalDateTime.now());
    }

    protected ExpiringToken(ExpiringToken token) {
        this(token.uuid, token.lifeSpan, token.dateCreated);
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
