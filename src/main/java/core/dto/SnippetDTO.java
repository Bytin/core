package core.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public record SnippetDTO(long id, @NonNull String title,
                @NonNull String language, String framework, @NonNull String code,
                @NonNull String description, String resource, @NonNull UserDTO owner,
                boolean hidden, @NonNull LocalDateTime whenCreated,
                @NonNull LocalDateTime whenLastModified) {
                        
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((code == null) ? 0 : code.hashCode());
                result = prime * result + ((description == null) ? 0 : description.hashCode());
                result = prime * result + ((framework == null) ? 0 : framework.hashCode());
                result = prime * result + ((language == null) ? 0 : language.hashCode());
                result = prime * result + ((resource == null) ? 0 : resource.hashCode());
                result = prime * result + ((title == null) ? 0 : title.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                SnippetDTO other = (SnippetDTO) obj;
                if (code == null) {
                        if (other.code != null)
                                return false;
                } else if (!code.equals(other.code))
                        return false;
                if (description == null) {
                        if (other.description != null)
                                return false;
                } else if (!description.equals(other.description))
                        return false;
                if (framework == null) {
                        if (other.framework != null)
                                return false;
                } else if (!framework.equals(other.framework))
                        return false;
                if (language == null) {
                        if (other.language != null)
                                return false;
                } else if (!language.equals(other.language))
                        return false;
                if (resource == null) {
                        if (other.resource != null)
                                return false;
                } else if (!resource.equals(other.resource))
                        return false;
                if (title == null) {
                        if (other.title != null)
                                return false;
                } else if (!title.equals(other.title))
                        return false;
                return true;
        }

                        
}
