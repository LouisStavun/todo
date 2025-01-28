package ch.cern.todo.repositories;

import ch.cern.todo.models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserApp, String> {

    UserApp findByUserName(String userName);
}
