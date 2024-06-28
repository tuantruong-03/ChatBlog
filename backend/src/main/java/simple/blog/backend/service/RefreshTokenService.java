package simple.blog.backend.service;

import org.springframework.stereotype.Service;

import simple.blog.backend.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByUsername(String username);
}
