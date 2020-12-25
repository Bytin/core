package core.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnippetDTO {

        @EqualsAndHashCode.Exclude
        private long id;

        @NonNull
        private String title, language, code, description;

        private String framework, resource;

        @NonNull
        @EqualsAndHashCode.Exclude
        private UserDTO owner;

        @NonNull
        @EqualsAndHashCode.Exclude
        private LocalDateTime whenCreated, whenLastModified;

        @EqualsAndHashCode.Exclude
        private boolean hidden;

}
