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

        // compare refresh token in database and refresh token in user request
        // valid that refresh token
        try {
            if (jwtUtil.isTokenValid(request.getToken())) {
                String username = jwtUtil.extractUsername(request.getToken());
                User user = userRepository.findByUsername(username);
                if (user == null) {
                    throw new AppException("User not found with username: " + username, HttpStatus.NOT_FOUND);
                }
                return RefreshTokenResponse.builder()
                        .newAccessToken(jwtUtil.generateAccessToken(user))
                        .build();
            }
            throw new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            // Token is not valid
            System.out.println("refresh token is not valid");
            refreshTokenRepository.deleteRefreshTokenByToken(request.getToken());
            throw new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }   
    }

    @Override
    public RefreshToken create(String username, String token) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findByUsername(username);
        if (existingRefreshToken != null) {
            refreshTokenRepository.deleteRefreshTokenByUsername(username);
        }
        RefreshToken refreshTokenModel = new RefreshToken(username, token);
        return refreshTokenRepository.save(refreshTokenModel);
    }
}
