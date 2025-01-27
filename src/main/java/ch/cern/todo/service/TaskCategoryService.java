package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;

import java.util.List;

public interface TaskCategoryService {
    void addTaskCategory(TaskCategory taskCategory);

    TaskCategory getById(int id);

    List<TaskCategory> getAllTasks();

    TaskCategory getByName(String name);

    TaskCategory createTaskCategory(String category_name, String categoryDescription);
}
