package ch.cern.todo.service;

import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserLoginService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Recherche utilisateur : " + username);
        UserApp user = userRepository.findByUserName(username);
        if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur ou mot de passe non trouvé");
        }
        //System.out.println("Utilisateur trouvé : " + user.getUserName() + " avec rôle : " + user.getRole());  // Debug: afficher l'utilisateur trouvé

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().name())
        );
    }
}
