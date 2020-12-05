package core.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Snippet {
    private long id;

    @NonNull
    private String title, language, code, description;

    private String framework, resource;

    @NonNull
    @EqualsAndHashCode.Exclude
    private User owner;

    @NonNull
    @EqualsAndHashCode.Exclude
    private LocalDateTime whenCreated, whenLastModified;

    private boolean hidden;

    public String getOwner() {
        return owner.getUsername();
    }

    public String getWhenLastModified() {
        return whenLastModified.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getWhenCreated() {
        return whenCreated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
