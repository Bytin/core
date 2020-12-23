package core.dto;

import lombok.NonNull;

public record UserDTO(long id, @NonNull String username, String email) {
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                UserDTO other = (UserDTO) obj;
                if (email == null) {
                        if (other.email != null)
                                return false;
                } else if (!email.equals(other.email))
                        return false;
                if (username == null) {
                        if (other.username != null)
                                return false;
                } else if (!username.equals(other.username))
                        return false;
                return true;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((email == null) ? 0 : email.hashCode());
                result = prime * result + ((username == null) ? 0 : username.hashCode());
                return result;
        }
        
}
