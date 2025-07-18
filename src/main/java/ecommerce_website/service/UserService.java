package ecommerce_website.service;

import ecommerce_website.model.Role;
import ecommerce_website.model.User;
import ecommerce_website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;
    
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already exists";
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        user.setOtp(otp);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER); // default fallback
        }
        user.setVerified(false);

        userRepository.save(user);

        //  Send OTP via email
        emailService.sendOtpEmail(user.getEmail(), otp);

        return "otp_sent";
    }

    public boolean verifyOtp(String email, String enteredOtp) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getOtp() != null && user.getOtp().equals(enteredOtp)) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
