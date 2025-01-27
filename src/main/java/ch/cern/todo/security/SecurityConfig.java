package ch.cern.todo.security;

import ch.cern.todo.enumeration.Role;
import ch.cern.todo.service.UserLoginService;
import ch.cern.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserLoginService customUserDetailsService; // Injecte ton CustomUserDetailsService

    public SecurityConfig(UserLoginService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;  // Injection via le constructeur
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF pour la console H2
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/h2-console/**").permitAll() // Permet l'accès à la console H2 sans authentification
                                .requestMatchers("/home/**").authenticated() // Exiger une authentification pour /home
                                .requestMatchers("/**").permitAll() // Autoriser tout le monde pour les autres routes
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/home", true) // Redirige vers /home après la connexion
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
                .httpBasic(withDefaults()) // Permet l'utilisation de Basic Auth
                .headers(AbstractHttpConfigurer::disable); // Désactive la gestion des en-têtes de sécurité

        return http.build();
    }

    @Bean
    @Lazy // To avoid circular dependency
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


}
