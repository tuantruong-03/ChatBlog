package simple.blog.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import simple.blog.backend.model.EmailVerificationToken;

@Repository
public interface EmailVerificationTokenRepository extends MongoRepository<EmailVerificationToken, Integer> {

	public EmailVerificationToken findByToken(String token);
	
	void deleteByUserEmail(String userEmail);
}
