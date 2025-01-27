package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.UserApp;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SearchService {

    List<Task> searchTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName);

    Long deleteTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName);

    Task updateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName);
}
