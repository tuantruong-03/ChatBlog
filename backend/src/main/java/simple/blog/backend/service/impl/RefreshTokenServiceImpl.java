package simple.blog.backend.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.model.RefreshToken;
import simple.blog.backend.repository.RefreshTokenRepository;
import simple.blog.backend.service.RefreshTokenService;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findByUsername(String username) {
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username);
        if (refreshToken == null) {
            throw new AppException("Refresh token doesn't exist with " + username, HttpStatus.UNAUTHORIZED);
        }
        return refreshToken;
    }

}
