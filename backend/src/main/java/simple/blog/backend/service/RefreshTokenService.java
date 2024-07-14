package simple.blog.backend.service;


import simple.blog.backend.dto.request.RefreshTokenRequest;
import simple.blog.backend.dto.response.RefreshTokenResponse;
import simple.blog.backend.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByUsername(String username);
    RefreshToken create(String username, String token);
    
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
