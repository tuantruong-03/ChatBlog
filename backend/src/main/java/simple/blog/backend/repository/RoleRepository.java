package simple.blog.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import simple.blog.backend.model.Role;

public interface RoleRepository extends MongoRepository<Role, Integer> {
    public Role findByAuthority(String authority);
    
} 
