package simple.blog.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;

}
