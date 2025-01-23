package ch.cern.todo.service;

import ch.cern.todo.model.Task;

import java.util.List;


public interface TaskService {

    void addTask(Task task);

    Task getById(int id);

    List<Task> getAllTasks();
}
