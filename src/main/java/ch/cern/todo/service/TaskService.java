package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.model.UserApp;

import java.sql.Timestamp;
import java.util.List;


public interface TaskService {

    void addTask(Task task);

    Task getById(int id);

    public List<Task> getTasksByName(String name);

    List<Task> getAllTasks();

    String toString();

    Task createTask(String taskName, String taskDescription, Timestamp deadline,
                    String taskCategoryName, UserApp user);
}
