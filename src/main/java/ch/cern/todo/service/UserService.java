package ch.cern.todo.service;

import ch.cern.todo.model.UserApp;

import java.util.List;

public interface UserService {
    void addUser(UserApp userApp);

    UserApp getById(int id);

    List<UserApp> getAllUsers();
}
