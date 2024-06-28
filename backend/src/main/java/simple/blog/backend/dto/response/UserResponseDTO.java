package simple.blog.backend.dto.response;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import simple.blog.backend.model.Role;
import simple.blog.backend.model.Status;

@Setter
@Getter
@Builder
public class UserResponseDTO {
    private Integer userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private Set<Role> roles;
    private Boolean isEnabled;
    private Status status;
}
