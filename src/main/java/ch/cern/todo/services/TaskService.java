package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.List;


public interface TaskService {

    void addTask(Task task);

    Task getById(int id);

    List<Task> getAllTasks();

    Task createTask(String taskName, String taskDescription, Timestamp deadline, String taskCategoryName, UserApp user);

    Long deleteTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName);

    void deleteTaskById(int id, UserApp user);

    Task updateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName);
}
