package core.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Builder
@RequiredArgsConstructor
@Value
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
