package simple.blog.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
	
	@NotNull(message = "Refresh token must be not null")
	private String refreshToken;

}
