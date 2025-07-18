package ecommerce_website.security;

import ecommerce_website.model.User;
import ecommerce_website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * This method loads a user from the database using their email address.
     * It wraps the user entity into a CustomUserDetails object for Spring Security.
     *
     * @param email the email used as the username during login
     * @return UserDetails implementation (CustomUserDetails)
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }

        return new CustomUserDetails(user); // ✅ Wrap your custom User in CustomUserDetails
    }
}
