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
     * @param categoryName
     * @param categoryDescription
     * @return the Task Category created
     */
    public TaskCategory createTaskCategory(String categoryName, String categoryDescription) {
        TaskCategory taskCategory = new TaskCategory(categoryName, categoryDescription);
        return taskCategoryRepository.save(taskCategory);
    }

    /**
     * Deletes a Task Category stored in the Database
     *
     * @param categoryName
     * @param currentUser
     * @throws TaskCategoryNotFoundException
     */
    @Override
    public void deleteTaskCategoryByName(String categoryName, UserApp currentUser) throws TaskCategoryNotFoundException {
        if (currentUser.isAdmin()) {
            TaskCategory taskCategory = taskCategoryRepository.findByCategoryName(categoryName);
            taskCategoryRepository.delete(taskCategory);
        }
    }

    @Override
    public void deleteTaskCategoryById(int categoryId, UserApp currentUser) {
        if (currentUser.isAdmin()) {
            Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(categoryId);
            taskCategory.ifPresent(category -> taskCategoryRepository.delete(category));
        }
    }

    /**
     * Updates a Task Category stored in the Database
     *
     * @param id
     * @param currentUser
     * @param categoryName
     * @param categoryDescription
     * @return the updated Task Category
     * @throws TaskCategoryNotFoundException
     */
    @Override
    public TaskCategory updateTaskCategory(int id, UserApp currentUser, String categoryName, String categoryDescription) throws TaskCategoryNotFoundException {
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
            }
            return taskCategoryRepository.save(taskCategoryToUpdate);
        }
        return null;
    }
}
