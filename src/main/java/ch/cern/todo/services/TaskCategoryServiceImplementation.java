package ch.cern.todo.services;

import ch.cern.todo.exceptions.TaskCategoryNotFoundException;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.TaskCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskCategoryServiceImplementation implements TaskCategoryService {

    TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public void setTaskRepository(TaskCategoryRepository taskCategoryRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @Override
    public void addTaskCategory(TaskCategory taskCategory) {
        taskCategoryRepository.save(taskCategory);
    }

    /**
     * Creates and saves a Task Category in the Database
     *
     * @param categoryName        the Category name
     * @param categoryDescription the Category description
     * @return the Task Category created.
     */
    public TaskCategory createTaskCategory(String categoryName, String categoryDescription) {
        TaskCategory taskCategory = new TaskCategory(categoryName, categoryDescription);
        return taskCategoryRepository.save(taskCategory);
    }

    /**
     * Retrieves a Task Category stored in the Database by its name and deletes it.
     *
     * @param categoryName the Category name
     * @param currentUser  the current Application user
     * @return a String according to the outcome of the deletion.
     * @throws TaskCategoryNotFoundException exception thrown if the category is not existing
     */
    @Override
    public String deleteTaskCategoryByName(String categoryName, UserApp currentUser) throws TaskCategoryNotFoundException {
        if (currentUser.isAdmin()) {
            TaskCategory taskCategory = taskCategoryRepository.findByCategoryName(categoryName);
            taskCategoryRepository.delete(taskCategory);
            return "SUCCESS";
        }
        return "NOT_ADMIN";
    }

    /**
     * Retrieves a Task Category stored in the Database by its ID and deletes it.
     *
     * @param categoryId  the Category ID
     * @param currentUser the current Application user
     * @return a String according to the outcome of the deletion.
     */
    @Override
    public String deleteTaskCategoryById(int categoryId, UserApp currentUser) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(categoryId);
        if (taskCategory.isPresent()) {
            if (currentUser.isAdmin()) {
                taskCategoryRepository.delete(taskCategory.get());
                return "SUCCESS";
            }
            return "NOT_ADMIN";
        }
        return "NOT_FOUND";
    }

    /**
     * Updates a Task Category stored in the Database
     *
     * @param id                  the Category ID
     * @param currentUser         the current Application user
     * @param categoryName        the Category name
     * @param categoryDescription the Category description
     * @return a String according to the outcome of the deletion.
     * @throws TaskCategoryNotFoundException exception thrown if the category is not existing
     */
    @Override
    public String updateTaskCategory(int id, UserApp currentUser, String categoryName, String categoryDescription) throws TaskCategoryNotFoundException {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(id);
        if (taskCategory.isPresent()) {
            TaskCategory taskCategoryToUpdate = taskCategory.get();
            if (currentUser.isAdmin()) {
                if (categoryName != null && !categoryName.isEmpty()) {
                    taskCategoryToUpdate.setCategoryName(categoryName);
                }
                if (categoryDescription != null && !categoryDescription.isEmpty()) {
                    taskCategoryToUpdate.setCategoryDescription(categoryDescription);
                }
                taskCategoryRepository.save(taskCategoryToUpdate);
                return "SUCCESS";
            }
            return "NOT_ALLOWED_USER";
        }
        return "NOT_FOUND";
    }
}


