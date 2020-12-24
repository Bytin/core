package core.dto;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class UserDTO {

        @EqualsAndHashCode.Exclude
        private long id;
        @NonNull
        private String username;
        private String email;

}
