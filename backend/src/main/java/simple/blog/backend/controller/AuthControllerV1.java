package simple.blog.backend.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import simple.blog.backend.dto.request.EmailTokenRequest;
import simple.blog.backend.dto.request.OAuthTokenRequest;
import simple.blog.backend.dto.request.RefreshTokenRequest;

import simple.blog.backend.dto.request.UserLoginRequest;
import simple.blog.backend.dto.request.UserRegistrationRequest;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.dto.response.UserLoginResponse;
import simple.blog.backend.dto.response.UserResponse;
import simple.blog.backend.service.RefreshTokenService;
import simple.blog.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerV1 {
	
	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/register") 
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegistrationRequest requestBody) throws UnsupportedEncodingException, MessagingException {
		UserResponse registeredUser = userService.register(requestBody);
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

	@PostMapping("/google-login")
	public ResponseEntity<ApiResponse> googleLogin(@Valid @RequestBody OAuthTokenRequest requestBody) {
		// 
		UserLoginResponse userLoginResponse = userService.googleLogin(requestBody.getToken());
		ApiResponse respponse = ApiResponse.builder()
    			.timestamp(LocalDateTime.now())
    			.message("User is authenticated")
    			.statusCode(HttpStatus.OK.value())
    			.data(userLoginResponse)
    			.build();
        return new ResponseEntity<>(respponse, HttpStatus.OK); 

	}
	@PostMapping("/facebook-login")
	public ResponseEntity<ApiResponse> faecbookLogin(@Valid @RequestBody OAuthTokenRequest requestBody) {
		UserLoginResponse userLoginResponse = userService.facebookLogin(requestBody.getToken());
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
	

}
