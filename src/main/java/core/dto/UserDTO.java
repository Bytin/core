package core.dto;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
public class UserDTO {

        @EqualsAndHashCode.Exclude
        private long id;
        @NonNull
        private String username;
        private String email;

}
