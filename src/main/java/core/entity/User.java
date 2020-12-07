package core.entity;

import core.dto.UserDTO;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {

    long id;

    @NonNull
    String username, password;

    String email;

    @NonNull
    UserRole role;

    public void validate(){
        if(username.isBlank())
            throw new IllegalArgumentException("Username can't be blank.");
        if(password.length() < 6)
            throw new IllegalArgumentException("Password should be at least 6 characters long.");
        //TODO validate email and password patterns
    }

    public UserDTO toUserDto(){
            return new UserDTO(id, username, email);
    }

    public enum UserRole {

        USER, ADMIN, SUSPENDED;

        /**
         * @return the string representation of this {@link UserRole}, as prefixed with
         *         "ROLE_"
         */
        public String getRole() {
            return "ROLE_" + name();
        }
    }
}
