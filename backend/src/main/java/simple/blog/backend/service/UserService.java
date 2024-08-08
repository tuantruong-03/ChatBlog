package simple.blog.backend.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import simple.blog.backend.dto.request.UserUpdateRequest;
import simple.blog.backend.model.User;

public interface UserService extends UserDetailsService {

    public List<User> findAllUsers();
    public User findUserByUserId(Integer userId);
    public User updateUser(Integer userId, UserUpdateRequest userCreationRequest);
    public void deleteUserById(Integer userId);
    public List<User> searchUsers(String query);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    


}
