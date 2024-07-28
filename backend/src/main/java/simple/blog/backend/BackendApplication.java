package simple.blog.backend;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import simple.blog.backend.enums.Provider;
import simple.blog.backend.model.Role;
import simple.blog.backend.model.User;
import simple.blog.backend.repository.RoleRepository;
import simple.blog.backend.repository.UserRepository;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role adminRole;
		Role userRole;
		if (roleRepository.findByAuthority("ROLE_ADMIN") == null) {
			adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
			System.out.println(adminRole);
		}

		if (roleRepository.findByAuthority("ROLE_USER") == null) {
			userRole = roleRepository.save(new Role("ROLE_USER"));
			System.out.println(userRole);
		}

		if (userRepository.findByUsername("admin") == null) {
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByAuthority("ROLE_ADMIN"));
			User admin = User.builder()
					.username("admin")
					.password(passwordEncoder.encode("123456"))
					.email("admin@gmail.com")
					.firstName("Im")
					.lastName("Admin")
					.profilePicture(User.DEFAULT_AVA_URL)
					.isEnabled(true)
					.roles(roles)
					.provider(Provider.LOCAL)
					.build();
			userRepository.save(admin);
		}
		if (userRepository.findByUserId(2) == null) {
			for (int i = 0; i < 5; ++i) {
				Set<Role> roles = new HashSet<>();
				roles.add(roleRepository.findByAuthority("ROLE_USER"));
				User user = User.builder()
						.username("user" + i)
						.password(passwordEncoder.encode("123456"))
						.email("user" + i + "@gmail.com")
						.firstName("Im")
						.lastName("User")
						.profilePicture(User.DEFAULT_AVA_URL)
						.isEnabled(true)
						.roles(roles)
						.provider(Provider.LOCAL)
						.build();
				userRepository.save(user);
			}
		}
	}
}
