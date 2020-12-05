package core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    long id;
    @NonNull
    String username;
    @NonNull
    String email;
    @NonNull
    String password;
    @NonNull
    UserRole role;

    public void validate(){
        if(username.isBlank() || email.isBlank())
            throw new IllegalArgumentException("Username or email can't be blank.");
        if(password.length() < 6)
            throw new IllegalArgumentException("Password should be at least 6 characters long.");
        //TODO validate email and password patterns
    }

    public enum UserRole {

        USER, ADMIN, UNACTIVATED, SUSPENDED;

        /**
         * @return the string representation of this {@link UserRole}, as prefixed with
         *         "ROLE_"
         */
        public String getRole() {
            return "ROLE_" + name();
        }
    }
}
