package simple.blog.backend.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import simple.blog.backend.dto.request.OAuthTokenRequest;
import simple.blog.backend.dto.request.RefreshTokenRequest;
import simple.blog.backend.dto.request.UserLoginRequest;
import simple.blog.backend.dto.request.UserLogoutRequest;
import simple.blog.backend.dto.request.UserRegistrationRequest;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.dto.response.UserLoginResponse;
import simple.blog.backend.dto.response.UserResponse;
import simple.blog.backend.mapper.UserMapper;
import simple.blog.backend.model.User;
import simple.blog.backend.service.EmailVerificationTokenService;
import simple.blog.backend.service.RefreshTokenService;
import simple.blog.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {
	
	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	private final EmailVerificationTokenService emailService;
	
	@PostMapping("/register") 
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegistrationRequest requestBody) throws UnsupportedEncodingException, MessagingException {
		UserResponse registeredUser = UserMapper.toUserResponse(userService.register(requestBody));
		System.out.println(registeredUser.toString());
    	ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User created successfully")
    			.statusCode(HttpStatus.CREATED.value())
    			.data(registeredUser)
    			.build();

        return new ResponseEntity<>(respponse, HttpStatus.CREATED); 
	}

	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody UserLoginRequest requestBody) {
		UserLoginResponse userLoginResponse = userService.login(requestBody);

		ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User is authenticated")
    			.statusCode(HttpStatus.OK.value())
    			.data(userLoginResponse)
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.OK); 
	}

	@DeleteMapping("/logout")
	public ResponseEntity<ApiResponse> logout(@Valid @RequestBody UserLogoutRequest requestBody) {
		userService.logout(requestBody);

		ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User is logged out")
    			.statusCode(HttpStatus.NO_CONTENT.value())
    			.data(null)
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.NO_CONTENT); 
	}

	@PostMapping("/google-login")
	public ResponseEntity<ApiResponse> GoogleLogin(@Valid @RequestBody OAuthTokenRequest requestBody) {
		// 
		UserLoginResponse userLoginResponse = userService.GoogleLogin(requestBody.getToken());
		ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User is authenticated")
    			.statusCode(HttpStatus.OK.value())
    			.data(userLoginResponse)
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.OK); 

	}
	@PostMapping("/facebook-login")
	public ResponseEntity<ApiResponse> FacebookLogin(@Valid @RequestBody OAuthTokenRequest requestBody) {
		UserLoginResponse userLoginResponse = userService.FacebookLogin(requestBody.getToken());
		ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User is authenticated")
    			.statusCode(HttpStatus.OK.value())
    			.data(userLoginResponse)
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.OK); 
	}

	
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
		System.out.println("refresh token controller");
		ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User is authenticated")
    			.statusCode(HttpStatus.OK.value())
    			.data(refreshTokenService.refreshToken(request))
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.OK); 
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse> sendPasswordResetEmail(@RequestParam String email) {
		
		User user = emailService.checkPasswordResetEmail(email);

		emailService.sendPasswordResetEmail(user);

        ApiResponse response = ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Password reset email sent successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

}
