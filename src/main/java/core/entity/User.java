package core.entity;

import core.dto.UserDTO;
import lombok.Data;
import lombok.NonNull;

@Data
public class User {

    final long id;

    @NonNull
    String username, email, password;

    @NonNull
    UserRole role;

    public void validate() {
        if (username.isBlank())
            throw new IllegalArgumentException("Username can't be blank.");
        if (email.isBlank())
            throw new IllegalArgumentException("Email can't be blank.");
        if (password.length() < 6)
            throw new IllegalArgumentException("Password should be at least 6 characters long.");
        // TODO validate email and password patterns
    }

    public UserDTO toUserDto() {
        return new UserDTO(id, username, email, role);
    }

    public enum UserRole {

        USER, ADMIN, SUSPENDED, UNACTIVATED;

        public String toString() {
            return "ROLE_" + name();
        }
    }
}
