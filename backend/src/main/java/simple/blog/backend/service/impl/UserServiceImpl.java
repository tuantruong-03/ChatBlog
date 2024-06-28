package simple.blog.backend.service.impl;


import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.UserRegistrationDTO;
import simple.blog.backend.dto.response.UserResponseDTO;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.mapper.UserMapper;
import simple.blog.backend.model.Role;
import simple.blog.backend.model.User;
import simple.blog.backend.repository.RoleRepository;
import simple.blog.backend.repository.UserRepository;
import simple.blog.backend.service.EmailVerificationTokenService;
import simple.blog.backend.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final EmailVerificationTokenService emailVerificationTokenService;


    @Override
    public List<UserResponseDTO> findAllUsers() {
    	List<User> usersList = userRepository.findAll();
    	return usersList.stream()
                .map(user -> userMapper.toUserResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException("User not found with username: " + username, HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public UserResponseDTO findUserByUserId(Integer userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new AppException("User not found with id: " + userId, HttpStatus.NOT_FOUND);
        }
        return userMapper.toUserResponse(user);
    }

	@Override
	public UserResponseDTO register(UserRegistrationDTO request) throws UnsupportedEncodingException, MessagingException {
		
		String username = request.getUsername();
		String email = request.getEmail();
		
		if(userRepository.existsByUsername(username)) {
			throw new AppException("User with username " +  username + " existed", HttpStatus.CONFLICT);
		}
		
		if(userRepository.existsByEmail(email)) {
			throw new AppException("User with email " +  email + " existed", HttpStatus.CONFLICT);
		}
		
		String encodedPassword = bcryptPasswordEncoder.encode(request.getPassword());
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepository.findByAuthority("ROLE_USER"));
		
		User user = User.builder()
				.lastName(request.getLastName())
				.firstName(request.getFirstName())
				.email(email)
				.username(username)
				.password(encodedPassword)
				.profilePicture(request.getProfilePicture())
				.status(null)
				.isEnabled(false)
				.roles(roles)
				.build();
		
		userRepository.save(user);
		
		emailVerificationTokenService.sendEmailConfirmation(email);
		
		return userMapper.toUserResponse(user);
	}


}
