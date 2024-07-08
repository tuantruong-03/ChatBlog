package simple.blog.backend.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
	
    private final UserService userService;
    
    @GetMapping()
    public ResponseEntity<ApiResponse> getUserList() {
    	ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Get users list successfully")
    			.statusCode(HttpStatus.OK.value())
    			.data(userService.findAllUsers())
    			.build();
    	
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserByUserId(@PathVariable Integer userId) {
    	ApiResponse resp = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("Get user successfully")
    			.statusCode(HttpStatus.OK.value())
    			.data(userService.findUserByUserId(userId))
    			.build();

        return new ResponseEntity<>(resp, HttpStatus.OK); 
    }
}
