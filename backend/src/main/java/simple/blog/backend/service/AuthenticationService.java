package simple.blog.backend.service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import simple.blog.backend.dto.request.UserLoginRequest;
import simple.blog.backend.dto.request.UserLogoutRequest;
import simple.blog.backend.dto.request.UserRegistrationRequest;
import simple.blog.backend.dto.response.UserLoginResponse;
import simple.blog.backend.model.User;

public interface AuthenticationService {

    public UserLoginResponse login(UserLoginRequest request);
    public void logout(UserLogoutRequest request);
    public UserLoginResponse GoogleLogin(String accessToken);
    public UserLoginResponse FacebookLogin(String accessToken);
    public User register(UserRegistrationRequest request) throws UnsupportedEncodingException, MessagingException;
    
}
