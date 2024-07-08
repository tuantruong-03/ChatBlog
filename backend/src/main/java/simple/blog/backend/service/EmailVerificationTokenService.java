package simple.blog.backend.service;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

import jakarta.mail.MessagingException;
import simple.blog.backend.model.EmailVerificationToken;

public interface EmailVerificationTokenService {
	
	public CompletableFuture<Void> sendEmailConfirmation(String recipientEmail) throws UnsupportedEncodingException, MessagingException;
	
	public boolean confirmAccountRegistration(String token) throws UnsupportedEncodingException, MessagingException;
	
	public boolean isTokenExpired(EmailVerificationToken token);
}
