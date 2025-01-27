package ch.cern.todo.service;

import ch.cern.todo.exception.TaskCategoryNotFoundException;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.TaskCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public TaskCategory getById(int taskCategory_id) throws TaskCategoryNotFoundException {
        return taskCategoryRepository.findById(taskCategory_id)
                .orElseThrow(() -> new TaskCategoryNotFoundException("Task Category not existing"));
    }

    @Override
    public TaskCategory getByName(String name) throws TaskCategoryNotFoundException {
        return taskCategoryRepository.findByCategoryName(name);
    }

    @Override
    public List<TaskCategory> getAllTasks() {
        return taskCategoryRepository.findAll();
    }

    public TaskCategory createTaskCategory(String category_name, String categoryDescription){
        TaskCategory taskCategory = new TaskCategory(category_name, categoryDescription);
        return taskCategoryRepository.save(taskCategory);
    }

    @Override
    public void deleteTaskCategory(String categoryName, UserApp currentUser) throws TaskCategoryNotFoundException {
        if (currentUser.isAdmin()){
            TaskCategory taskCategory = taskCategoryRepository.findByCategoryName(categoryName);
            taskCategoryRepository.delete(taskCategory);
        }
    }

    @Override
    public TaskCategory updateTaskCategory(int id, UserApp currentUser, String categoryName, String categoryDescription) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(id);
        if(taskCategory.isPresent()){
            TaskCategory taskCategoryToUpdate = taskCategory.get();
            if (currentUser.isAdmin()){
                if (categoryName != null && !categoryName.isEmpty()){
                    taskCategoryToUpdate.setCategoryName(categoryName);
                }
                if (categoryDescription != null && !categoryDescription.isEmpty()){
                    taskCategoryToUpdate.setCategoryDescription(categoryDescription);
                }
            }
            return taskCategoryRepository.save(taskCategoryToUpdate);
        }
        return null;
    }
}
