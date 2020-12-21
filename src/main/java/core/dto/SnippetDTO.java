package core.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public record SnippetDTO(long id, @NonNull String title, @NonNull String language, String framework,
            @NonNull String code, @NonNull String description, String resource, @NonNull UserDTO owner, boolean hidden,
            @NonNull LocalDateTime whenCreated, @NonNull LocalDateTime whenLastModified){ }
