package ch.cern.todo.model;

import ch.cern.todo.enumeration.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class UserApp implements UserDetails {

    @Id
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Role role;

    public UserApp(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public UserApp() {

    }

    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());  // Ajout de l'autorité basée sur le rôle
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;  // Retourne le nom d'utilisateur
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
