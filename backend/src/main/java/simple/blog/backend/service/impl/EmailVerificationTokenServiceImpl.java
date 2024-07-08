package simple.blog.backend.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.model.EmailVerificationToken;
import simple.blog.backend.model.User;
import simple.blog.backend.repository.EmailVerificationTokenRepository;
import simple.blog.backend.repository.UserRepository;
import simple.blog.backend.service.EmailVerificationTokenService;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

	private final JavaMailSender javaMailSender;
	private final EmailVerificationTokenRepository emailVerificationTokenRepository;
	private final UserRepository userRepository;
	
	@Async
	@Override
	public CompletableFuture<Void> sendEmailConfirmation(String recipientEmail) throws UnsupportedEncodingException, MessagingException {
	    String token = UUID.randomUUID().toString();
	    
	    EmailVerificationToken emailVerificationToken = new EmailVerificationToken(recipientEmail, token);
	    emailVerificationTokenRepository.save(emailVerificationToken);
	    
	    MimeMessage message = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);

	    helper.setFrom("contact@simpleblog.com", "Simple Blog Support");
	    helper.setTo(recipientEmail);

	    String subject = "Please confirm your account";
	    String link = "http://localhost:3000/confirm-account?token=" + token;

	    String content = "<p>Hello,</p>"
	                    + "<p>Thank you for registering with Simple Blog.</p>"
	                    + "<p>Please click the link below to confirm your account:</p>"
	                    + "<p><a href=\"" + link + "\">Confirm my account</a></p>"
	                    + "<br>"
	                    + "<p>If you did not register for this account, please ignore this email.</p>";

	    helper.setSubject(subject);

	    helper.setText(content, true);
	    javaMailSender.send(message);
		return CompletableFuture.completedFuture(null);
	}
	
	@Override
    public boolean isTokenExpired(EmailVerificationToken token) {
        
        Date expiryDate = token.getExpiryDate();
        Date currentDate = new Date();

        return currentDate.after(expiryDate);
    }

	@Override
	public boolean confirmAccountRegistration(String token) throws UnsupportedEncodingException, MessagingException {
		
        EmailVerificationToken emailToken = emailVerificationTokenRepository.findByToken(token);
		System.out.println("emailToken " + emailToken);
        
        if (emailToken == null) {
            throw new AppException("Email token not found", HttpStatus.NOT_FOUND);
        }
        
        User user = userRepository.findByEmail(emailToken.getUserEmail());
        if (user == null) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
        
		// if the token has not expired
	    if (!isTokenExpired(emailToken)) {
	        // activate user account
	        user.setIsEnabled(true);
	        userRepository.save(user);

	        // Delete token from the database
	        emailVerificationTokenRepository.deleteByUserEmail(user.getEmail());
	        return true;
	    } 
	    else {
	    	// delete old token
	    	emailVerificationTokenRepository.deleteByUserEmail(user.getEmail());
	    	// send new email
	    	sendEmailConfirmation(user.getEmail());
	    	return false;
	    }
	}
}
