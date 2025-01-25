package ch.cern.todo.service;

import ch.cern.todo.exception.UserNotFoundException;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(UserApp userApp) {
        userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));
        userRepository.save(userApp);
    }

    @Override
    public UserApp getById(int user_id) throws UserNotFoundException {
    /*    return userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("UserApp not existing"));
    */
        return null;
    }


    @Override
    public List<UserApp> getAllUsers() {
        return userRepository.findAll();
    }
}
