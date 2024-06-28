package simple.blog.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import simple.blog.backend.model.EmailVerificationToken;

public interface EmailVerificationTokenRepository extends MongoRepository<EmailVerificationToken, Integer> {

	public EmailVerificationToken findByToken(String token);
}
