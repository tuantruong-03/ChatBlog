package simple.blog.backend.service;

import java.util.concurrent.CompletableFuture;

import simple.blog.backend.model.EmailVerificationToken;
import simple.blog.backend.model.User;

public interface EmailVerificationTokenService {
	
	public CompletableFuture<Void> sendEmailConfirmation(String recipientEmail);
	
	public boolean confirmAccountRegistration(String token);
	
	public boolean isTokenExpired(EmailVerificationToken token);
	
	
	public User checkPasswordResetEmail(String email);
	
	public CompletableFuture<Void> sendPasswordResetEmail(User user);
	
	public boolean resetPassword(String token, String password);
}
