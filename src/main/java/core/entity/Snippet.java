package core.entity;

import java.time.LocalDateTime;
import java.util.regex.Pattern;
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
        @EqualsAndHashCode.Exclude
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

        @EqualsAndHashCode.Exclude
        private boolean hidden;

        public User getOwner() {
                return owner;
        }

        public boolean contains(String str){
            return findWherein(value -> value != null && value.contains(str));
        }

        public boolean containsPattern(String regex){
            return findWherein(string -> string != null && Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(string).find());
        }

        public boolean findWherein(OnSnippetField<Boolean> on){
            for (String value : new String[]{title, language, code, description, framework, resource}) {
                boolean gotIt = on.value(value);
                if(gotIt)
                    return gotIt;
            }
            return false;
        }

        public SnippetDTO toSnippetDto() {
                return SnippetDTO.builder().id(id).title(title).language(language)
                                .framework(framework).code(code).description(description)
                                .resource(resource).owner(owner.toUserDto()).hidden(hidden)
                                .whenCreated(whenCreated).whenLastModified(whenLastModified)
                                .build();
        }

        @FunctionalInterface
        private interface OnSnippetField<T>{
            T value(String value);
        }

}
