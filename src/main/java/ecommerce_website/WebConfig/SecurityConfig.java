package ecommerce_website.WebConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ecommerce_website.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/",
                		"/register",
                		"/register/**",
                		"/login",
                		"/verify-otp",
                		"/thankyou", "/css/**"
                		, "/js/**"
                		, "/images/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/seller/**").hasRole("SELLER")
                .requestMatchers("/customer/**").hasRole("CUSTOMER")
                .requestMatchers("/cart/**", "/checkout/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/register")                   // 👈 Same page for login
                .loginProcessingUrl("/login")             // 👈 Spring handles POST /login
                .defaultSuccessUrl("/default", true)             // 👈 On successful login
                .failureUrl("/register?error=true")       // 👈 On login failure
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
                )
            .userDetailsService(userDetailsService); // 👈 Connect custom user service

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
