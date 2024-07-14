package simple.blog.backend.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.mail.MessagingException;
import simple.blog.backend.dto.request.UserLoginRequest;
import simple.blog.backend.dto.request.UserRegistrationRequest;
import simple.blog.backend.dto.response.UserLoginResponse;
import simple.blog.backend.dto.response.UserResponse;

public interface UserService extends UserDetailsService {

    public List<UserResponse> findAllUsers();
    public UserResponse findUserByUserId(Integer userId);

    public UserLoginResponse login(UserLoginRequest request);
    public UserLoginResponse googleLogin(String accessToken);
    public UserLoginResponse facebookLogin(String accessToken);
    public UserResponse register(UserRegistrationRequest request) throws UnsupportedEncodingException, MessagingException;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;



}
