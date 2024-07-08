package simple.blog.backend.mapper;

import org.mapstruct.Mapper;

import simple.blog.backend.dto.response.UserResponse;
import simple.blog.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
   //User toUser(UserRegistrationDTO request);
	
	UserResponse toUserResponse(User user);

}
