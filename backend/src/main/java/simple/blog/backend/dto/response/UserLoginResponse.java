package simple.blog.backend.dto.response;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import simple.blog.backend.model.Role;

@Setter
@Getter
@Builder
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;
    private Set<Role> roles;
}
