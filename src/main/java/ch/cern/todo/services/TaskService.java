package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;

import java.sql.Timestamp;
import java.util.List;


public interface TaskService {

    void addTask(Task task);

    List<Task> getAllTasks();

    Task createTask(String taskName, String taskDescription, Timestamp deadline, String taskCategoryName, UserApp user);

    String deleteTasks(UserApp user, String name, String description, String deadline, String username, String categoryName);

    String deleteTaskById(int id, UserApp user);

    String partialUpdateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName);

    String completeUpdateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName);
}
