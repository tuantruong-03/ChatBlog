package simple.blog.backend.mapper;



import simple.blog.backend.dto.response.UserResponse;
import simple.blog.backend.model.User;

public class UserMapper {

   // User toUser(UserRegistrationDTO request);

   public static UserResponse toUserResponse(User user) {
      if (user == null) {
         return null;
      }
      UserResponse.UserResponseBuilder userResponse = UserResponse.builder();
      userResponse.email(user.getEmail());
      userResponse.firstName(user.getFirstName());
      userResponse.isEnabled(user.getIsEnabled());
      userResponse.lastName(user.getLastName());
      userResponse.profilePicture(user.getProfilePicture());
      userResponse.status(user.getStatus());
      userResponse.userId(user.getUserId());
      userResponse.username(user.getUsername());

      return userResponse.build();

   }

   // public static User toModelFromCreationRequest(UserUpdateRequest userUpdateRequest) {
   //    if (userUpdateRequest == null) {
   //       return null;
   //    } 
   // }
}
