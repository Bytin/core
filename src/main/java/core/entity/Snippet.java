package core.entity;

import java.time.LocalDateTime;
import core.dto.SnippetDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
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

        public User getOwner() {
                return owner;
        }

        public SnippetDTO toSnippetDto() {
                return new SnippetDTO(id, title, language, framework, code, description, resource,
                                owner.toUserDto(), hidden, whenCreated, whenLastModified);
        }

}
