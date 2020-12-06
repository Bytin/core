package core.dto;

import java.time.LocalDateTime;

import core.entity.Snippet;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SnippetDTO(long id, @NonNull String title, @NonNull String language, String framework,
            @NonNull String code, @NonNull String description, String resource, @NonNull String owner, boolean hidden,
            @NonNull LocalDateTime whenCreated, @NonNull LocalDateTime whenLastModified){

        public SnippetDTO(Snippet snippet) {
            this(snippet.getId(), snippet.getTitle(), snippet.getLanguage(), snippet.getFramework(), snippet.getCode(), snippet.getDescription(),
                    snippet.getResource(), snippet.getOwner(), snippet.isHidden(), snippet.getWhenCreated(),
                    snippet.getWhenLastModified());
        }
    
}
