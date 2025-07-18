package ecommerce_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce_website.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 User findByEmail(String email);
	 
}
