package simple.blog.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import simple.blog.backend.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    RefreshToken findByUsername(String username);
    RefreshToken findByToken(String token);
    Long deleteRefreshTokenByUsername(String username); // A numeric return type directly removes the matching documents returning the total number of documents removed.
    Long deleteRefreshTokenByToken(String token); // A numeric return type directly removes the matching documents returning the total number of documents removed.
}
