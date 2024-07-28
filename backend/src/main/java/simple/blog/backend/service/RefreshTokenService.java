package simple.blog.backend.service;


import simple.blog.backend.dto.request.RefreshTokenRequest;
import simple.blog.backend.dto.response.RefreshTokenResponse;
import simple.blog.backend.model.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken findByUsername(String username);
    public RefreshToken create(String username, String token);
    
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
