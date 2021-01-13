package core.dto;

import core.entity.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

        @EqualsAndHashCode.Exclude
        private long id;
        @NonNull
        private String username, email;
        private UserRole role;

}
