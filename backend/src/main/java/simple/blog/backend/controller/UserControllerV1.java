package simple.blog.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.UserUpdateRequest;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.dto.response.UserResponse;
import simple.blog.backend.mapper.ChatRoomMapper;
import simple.blog.backend.mapper.UserMapper;
import simple.blog.backend.model.ChatRoom;
import simple.blog.backend.model.User;
import simple.blog.backend.service.ChatRoomService;
import simple.blog.backend.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
	
    private final UserService userService;
	private final ChatRoomService chatRoomService;
    
    @GetMapping()
    public ResponseEntity<ApiResponse> getUserList() {
    	ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Get users list successfully")
    			.statusCode(HttpStatus.OK.value())
    			.data(userService.findAllUsers().stream().map(user -> UserMapper.toUserResponse(user)).collect(Collectors.toList()))
    			.build();
    	
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
	@GetMapping("/me")
	public ResponseEntity<ApiResponse> getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Get user successfully")
    			.statusCode(HttpStatus.OK.value())
    			.data(UserMapper.toUserResponse(user))
    			.build();
    	
        return new ResponseEntity<>(resp, HttpStatus.OK);
	}


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserByUserId(@PathVariable("id") @Min(0) Integer id) {
		UserResponse userResponse = UserMapper.toUserResponse((User)userService.findUserByUserId(id));
    	ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Get user successfully")
    			.statusCode(HttpStatus.OK.value())
    			.data(userResponse)
    			.build();

        return new ResponseEntity<>(resp, HttpStatus.OK); 
    }
    
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest userUpdateRequest ) {

		UserResponse userResponse = UserMapper.toUserResponse(userService.updateUser(id, userUpdateRequest));
		ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User updated")
    			.statusCode(HttpStatus.CREATED.value())
    			.data(userResponse)
    			.build();

        return new ResponseEntity<>(resp, HttpStatus.CREATED); 
	} 

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
		userService.deleteUserById(id);

		ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User deleted")
    			.statusCode(HttpStatus.OK.value())
    			.data(null)
    			.build();

        return new ResponseEntity<>(resp, HttpStatus.OK); 
	} 

	
    @GetMapping("/{id}/chatrooms")
    public ResponseEntity<ApiResponse> getChatRoomsOfUserId(@PathVariable("id") @Min(0) Integer id) {
		List<ChatRoom> chatRooms = chatRoomService.findChatRoomsOfUser(id);
    	ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Get user successfully")
    			.statusCode(HttpStatus.OK.value())
    			.data(chatRooms.stream().map(chatRoom -> ChatRoomMapper.toResponseFromModel(chatRoom)))
    			.build();

        return new ResponseEntity<>(resp, HttpStatus.OK); 
    }
	
	@GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUsers(@RequestParam String query) {
		ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.statusCode(HttpStatus.OK.value())
    			.data(userService.searchUsers(query).stream().map(user -> UserMapper.toUserResponse(user)))
    			.build();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
