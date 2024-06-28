package simple.blog.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import simple.blog.backend.model.RefreshToken;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    RefreshToken findByUsername(String username);
    RefreshToken findByToken(String token);
}
