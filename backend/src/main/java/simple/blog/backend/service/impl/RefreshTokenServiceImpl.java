package simple.blog.backend.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.dto.request.RefreshTokenRequest;
import simple.blog.backend.dto.response.RefreshTokenResponse;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.model.RefreshToken;
import simple.blog.backend.model.User;
import simple.blog.backend.repository.RefreshTokenRepository;
import simple.blog.backend.repository.UserRepository;
import simple.blog.backend.service.RefreshTokenService;
import simple.blog.backend.util.JwtUtil;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    @Override
    public RefreshToken findByUsername(String username) {
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username);
        if (refreshToken == null) {
            throw new AppException("Refresh token doesn't exist with " + username, HttpStatus.UNAUTHORIZED);
        }
        return refreshToken;
    }

	@Override
	public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
		
		String username = jwtUtil.extractUsername(request.getRefreshToken());
		
		User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException("User not found with username: " + username, HttpStatus.NOT_FOUND);
        }
        
        String storedRefreshToken = refreshTokenRepository.findByUsername(username).getToken();
        
        // compare refresh token in database and refresh token in user request
        // valid that refresh token
        if (storedRefreshToken.equals(request.getRefreshToken()) && jwtUtil.isTokenValid(request.getRefreshToken())) {
            return RefreshTokenResponse.builder()
	            		.newAccessToken(jwtUtil.generateAccessToken(user))
	            		.refreshToken(storedRefreshToken)
            		    .build();
        }

        throw new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
	}
}
