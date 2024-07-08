package simple.blog.backend.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.EmailTokenRequest;
import simple.blog.backend.dto.response.ApiResponse;
import simple.blog.backend.service.EmailVerificationTokenService;

@RestController
@RequestMapping("/api/v1/verification")
@RequiredArgsConstructor
public class VerificationControllerV1 {
	
	private final EmailVerificationTokenService emailVerificationTokenService;
	
	
	
	@PostMapping("/account-registration")
	public ResponseEntity<ApiResponse> confirmAccountRegistration(@Valid @RequestBody EmailTokenRequest tokenDTO) throws UnsupportedEncodingException, MessagingException {
		System.out.println("account-registration");
		System.out.println(tokenDTO.getToken());
		boolean isVerified = emailVerificationTokenService.confirmAccountRegistration(tokenDTO.getToken());
		String message = isVerified ? "Account is confirmed successfully" : "Token expired. A new confirmation email has been sent.";
		int statusCode = isVerified ? HttpStatus.OK.value() : HttpStatus.ACCEPTED.value();

		ApiResponse response = ApiResponse.builder()
				.statusCode(statusCode)
				.message(message)
				.build();
		return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
		
	}

}