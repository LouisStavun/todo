package ch.cern.todo.security;

import ch.cern.todo.services.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private final UserLoginService customUserDetailsService; // Injecte ton CustomUserDetailsService

    public SecurityConfig(UserLoginService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Filters HTTP links to ensure authentication.
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/h2-console/**").permitAll() // User can access DB without having to be authenticated from UserApp
                                .requestMatchers("/home/**").authenticated() // Authorize access to /home only for authenticated Users
                                .requestMatchers("/**").permitAll() // Autoriser tout le monde pour les autres routes
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/home", true) // After Login, the user is redirected to /home
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
                .httpBasic(withDefaults()) // Allow Basic Auth to test in Postman
                .headers(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Encodes passwords before storing in Database.
     *
     * @return Password Encoder
     */
    @Bean
    @Lazy // To avoid circular dependency
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Loads User and encodes its password in the Database.
     *
     * @return authentication information.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


}
