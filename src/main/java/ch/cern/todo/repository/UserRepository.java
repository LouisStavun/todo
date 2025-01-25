package ch.cern.todo.repository;

import ch.cern.todo.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, String> {

    UserApp findByUserName(String userName);
}
