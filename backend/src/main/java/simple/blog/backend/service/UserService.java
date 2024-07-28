package simple.blog.backend.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.mail.MessagingException;
import simple.blog.backend.dto.request.UserUpdateRequest;
import simple.blog.backend.dto.request.UserLoginRequest;
import simple.blog.backend.dto.request.UserLogoutRequest;
import simple.blog.backend.dto.request.UserRegistrationRequest;
import simple.blog.backend.dto.response.UserLoginResponse;
import simple.blog.backend.model.User;

public interface UserService extends UserDetailsService {

    public List<User> findAllUsers();
    public User findUserByUserId(Integer userId);
    public User updateUser(Integer userId, UserUpdateRequest userCreationRequest);
    public void deleteUserById(Integer userId);
    public List<User> searchUsers(String query);
 
    public UserLoginResponse login(UserLoginRequest request);
    public void logout(UserLogoutRequest request);
    public UserLoginResponse GoogleLogin(String accessToken);
    public UserLoginResponse FacebookLogin(String accessToken);
    public User register(UserRegistrationRequest request) throws UnsupportedEncodingException, MessagingException;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    


}
