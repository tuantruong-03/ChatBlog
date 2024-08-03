package simple.blog.backend.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.UserLoginRequest;
import simple.blog.backend.dto.request.UserLogoutRequest;
import simple.blog.backend.dto.request.UserRegistrationRequest;
import simple.blog.backend.dto.response.FacebookUserInfoResponse;
import simple.blog.backend.dto.response.GoogleUserInfoResponse;
import simple.blog.backend.dto.response.UserLoginResponse;
import simple.blog.backend.enums.Provider;
import simple.blog.backend.enums.Status;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.model.RefreshToken;
import simple.blog.backend.model.Role;
import simple.blog.backend.model.User;
import simple.blog.backend.repository.RefreshTokenRepository;
import simple.blog.backend.repository.RoleRepository;
import simple.blog.backend.repository.UserRepository;
import simple.blog.backend.service.AuthenticationService;
import simple.blog.backend.service.EmailVerificationTokenService;
import simple.blog.backend.service.UserService;
import simple.blog.backend.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final EmailVerificationTokenService emailVerificationTokenService;
	private final UserService userService;
	private final UserRepository userRepository;
	
	@Override
	public User register(UserRegistrationRequest request)
			throws UnsupportedEncodingException, MessagingException {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new AppException("User with username " + request.getUsername() + " existed", HttpStatus.CONFLICT);
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new AppException("User with email " + request.getEmail() + " existed", HttpStatus.CONFLICT);
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepository.findByAuthority("ROLE_USER"));

		User user = User.builder()
				.lastName(request.getLastName())
				.firstName(request.getFirstName())
				.email(request.getEmail())
				.username(request.getUsername())
				.password(encodedPassword)
				.profilePicture(request.getProfilePicture())
				.status(null)
				.isEnabled(false)
				.provider(request.getProvider())
				.roles(roles)
				.build();

		userRepository.save(user);

		emailVerificationTokenService.sendEmailConfirmation(request.getEmail());

		return user;
	}

	private UserLoginResponse getUserLoginResponse(User user) {
		String username = user.getUsername();
		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);

		// Delete old token then create and store refreshTokenModel in Server
		RefreshToken existingRefreshToken = refreshTokenRepository.findByUsername(username);
		if (existingRefreshToken != null) {
			refreshTokenRepository.deleteRefreshTokenByUsername(username);
		}
		RefreshToken refreshTokenModel = new RefreshToken(username, refreshToken);
		refreshTokenRepository.save(refreshTokenModel);
		UserLoginResponse userLoginResponse = UserLoginResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.roles(user.getRoles())
				.build();
		
		return userLoginResponse;
	}

	@Override
	public UserLoginResponse login(UserLoginRequest request) {
		System.out.println("login service");
		String username = request.getUsername();
		String rawPassword = request.getPassword();
		User user =  (User) userService.loadUserByUsername(username);

		if (!user.isEnabled()) {
			throw new AppException("Please check your email to verify your account", HttpStatus.UNAUTHORIZED);
		}
		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new AppException("Invalid username or password", HttpStatus.UNAUTHORIZED);
		}
		
		user.setStatus(Status.ONLINE);
		
		return getUserLoginResponse(user);
	}

	@Override
	public UserLoginResponse GoogleLogin(String accessToken) {
		final String googleUserInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
		RestTemplate restTemplate = new RestTemplate();

		// Set up headers
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Set up request entity
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		// Make the request
		ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(googleUserInfoEndpoint, HttpMethod.GET,
				httpEntity, GoogleUserInfoResponse.class);
		if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
			throw new AppException("Token is not valid", HttpStatus.UNAUTHORIZED);
		}
		GoogleUserInfoResponse userResponse = response.getBody();

		if (userRepository.existsByEmail(userResponse.getEmail())) {
			User user = userRepository.findByEmail(userResponse.getEmail());
			return getUserLoginResponse(user);

		} else {
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByAuthority("ROLE_USER"));
			User user = User.builder()
					.email(userResponse.getEmail())
					.firstName(userResponse.getGivenName())
					.lastName(userResponse.getFamilyName())
					.username(userResponse.getEmail())
					.profilePicture(userResponse.getPicture())
					.provider(Provider.GOOGLE)
					.roles(roles)
					.password("")
					.isEnabled(userResponse.isEmailVerified())
					.status(Status.ONLINE)
					.build();
			userRepository.save(user);
			return getUserLoginResponse(user);
		}
	}

	@Override
	public UserLoginResponse FacebookLogin(String accessToken) {
		final String facebookUserInfoEndpoint = "https://graph.facebook.com/me?fields=id,first_name,last_name,email,picture";
		RestTemplate restTemplate = new RestTemplate();

		// Set up headers
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Set up request entity
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<FacebookUserInfoResponse> response = restTemplate.exchange(facebookUserInfoEndpoint, HttpMethod.GET, httpEntity,
		FacebookUserInfoResponse.class);
		if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
			throw new AppException("Token is not valid", HttpStatus.UNAUTHORIZED);
		}
		FacebookUserInfoResponse userResponse = response.getBody();
		if (userRepository.existsByEmail(userResponse.getEmail())) {
			User user = userRepository.findByEmail(userResponse.getEmail());
			return getUserLoginResponse(user);

		} else {
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByAuthority("ROLE_USER"));
			User user = User.builder()
					.email(userResponse.getEmail())
					.firstName(userResponse.getFirstName())
					.lastName(userResponse.getLastName())
					.username(userResponse.getEmail())
					.profilePicture(userResponse.getPictureUrl())
					.provider(Provider.FACEBOOK)
					.roles(roles)
					.password("")
					.status(Status.ONLINE)
					.isEnabled(true)
					.build();
			userRepository.save(user);
			return getUserLoginResponse(user);
		}
	}

	@Override
	public void logout(UserLogoutRequest request) {
		refreshTokenRepository.deleteRefreshTokenByToken(request.getRefreshToken());
	}

}
