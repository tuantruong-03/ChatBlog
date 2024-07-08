package simple.blog.backend.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import simple.blog.backend.enums.Provider;
import simple.blog.backend.validation.EnumValue;

@Getter
public class UserRegistrationRequest {
    
    @NotBlank(message =  "Username must be not blank!")
    @NotNull(message =  "Username must be not empty!")
    private String username;
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message =  "First name must be not blank!")
    @NotNull
    @Pattern(regexp = "([A-Z][a-z]*)", message = "First letter  of first name must be capitalized!")
    private String firstName;
    
    @NotBlank(message =  "Last name must be not blank!")
    @NotNull
    @Pattern(regexp = "([A-Z][a-z]*)", message = "First letter of last name must be capitalized!")
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 15, message = "Password must be from 5 to 15 letters!")
    private String password;

    private String profilePicture;

//    @ValidProvider
    @EnumValue(name = "provider", enumClass = Provider.class)
    @NotNull
    private Provider provider;

}
