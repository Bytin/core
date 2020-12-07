package core.dto;

import lombok.NonNull;

public record UserDTO(long id, @NonNull String username, String email) {
        
}
