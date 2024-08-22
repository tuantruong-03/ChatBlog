package simple.blog.backend.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import simple.blog.backend.enums.Status;

@Setter
@Getter
@ToString
@Builder
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private Boolean isEnabled;
    private Status status;
}
