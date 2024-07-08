package simple.blog.backend.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailTokenRequest {
	@NotNull(message = "Token is invalid!")
	@NotBlank(message = "Token is invalid!")
	private String token;

}
