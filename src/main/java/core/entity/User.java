package core.entity;

import core.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
public class User {

    final long id;

    @NonNull
    String username, password;

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
            return new UserDTO(id, username);
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
