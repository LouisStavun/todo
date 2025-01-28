package ch.cern.todo.services;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;

public interface TaskCategoryService {
    void addTaskCategory(TaskCategory taskCategory);

    TaskCategory createTaskCategory(String category_name, String categoryDescription);

    void deleteTaskCategory(String categoryName, UserApp currentUser);

    TaskCategory updateTaskCategory(int id, UserApp currentUser, String categoryName, String categoryDescription);
}
