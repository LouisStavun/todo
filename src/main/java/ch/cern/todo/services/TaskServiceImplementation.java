package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.SearchRepository;
import ch.cern.todo.repositories.TaskCategoryRepository;
import ch.cern.todo.repositories.TaskRepository;
import ch.cern.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskCategoryService taskCategoryService;

    @Autowired
    SearchService searchService;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(Task task) {
        taskRepository.save(task);
    }


    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves Task Category by name, if it doesn't exist, creates it.
     *
     * @param taskCategoryName the Task Category Name
     * @return the Task Category.
     */
    private TaskCategory getTaskCategory(String taskCategoryName) {
        TaskCategory taskCategory = taskCategoryRepository.findByCategoryName(taskCategoryName);
        // If the Task Category specified by the user is not already existing
        if (taskCategory == null) {
            // Creates it
            taskCategory = taskCategoryService.createTaskCategory(taskCategoryName, null);
        }
        return taskCategory;
    }

    /**
     * Creates and saves a Task in the Database. If the user specifies a Task Category that does not exist, creates it.
     *
     * @param name         the task name
     * @param description  the task description
     * @param deadline     the task deadline
     * @param categoryName the task category name
     * @param user         the ask assigned user
     * @return the created Task.
     */
    public Task createTask(String name, String description, Timestamp deadline,
                           String categoryName, UserApp user) {

        TaskCategory taskCategory = getTaskCategory(categoryName);
        Task task = new Task(name, description, deadline, taskCategory, user);
        return taskRepository.save(task);
    }


    /**
     * Retrieves Tasks corresponding to all the argument stored in the Database.
     *
     * @param user         the current user of the application
     * @param name         the task name
     * @param description  the task description
     * @param deadline     the task deadline
     * @param username     the task assigned user
     * @param categoryName the task category
     * @return a String according to the outcome of the deletion.
     */
    @Override
    public String deleteTasks(UserApp user, String name, String description, String deadline, String username, String categoryName) {
        Specification<Task> spec = searchService.filterTasks(user, name, description, deadline, username, categoryName);
        long result = searchRepository.delete(spec);
        if (result == 1) {
            return "SUCCESS";
        }
        return "FAILURE";
    }

    /**
     * Retrieves and deletes a Task by its ID.
     *
     * @param id          the id of the task
     * @param currentUser the current user of the application
     * @return a String according to the outcome of the deletion.
     */
    @Override
    public String deleteTaskById(int id, UserApp currentUser) {
        Optional<Task> taskById = taskRepository.findById(id);
        if (taskById.isPresent()) {
            Task task = taskById.get();
            if (currentUser.isAdmin() || task.getUserAssigned().getUsername().equals(currentUser.getUsername())) {
                taskRepository.delete(task);
                return "SUCCESS";
            }
            return "NOT_ADMIN";
        }
        return "NOT_FOUND";
    }


    /**
     * Updates a Task stored in the Database.
     *
     * @param id           the id of the task
     * @param user         the current user of the application
     * @param name         the new name of the task
     * @param description  the new description of the task
     * @param deadline     the new deadline of the task
     * @param username     the new user assigned to the task
     * @param categoryName the new category of the task
     * @return a String according to the outcome of the update.
     */
    @Override
    public String partialUpdateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task taskToUpdate = task.get();
            // If the user is an Admin OR if he is the one assigned to the task
            if (user.isAdmin() || taskToUpdate.getUserAssigned().getUsername().equals(user.getUsername())) {
                // The user can modify the Task
                if (name != null && !name.isEmpty()) {
                    taskToUpdate.setTaskName(name);
                }
                if (description != null && !description.isEmpty()) {
                    taskToUpdate.setTaskDescription(description);
                }
                if (deadline != null && !deadline.isEmpty()) {
                    taskToUpdate.setDeadline(Timestamp.valueOf(deadline));
                }
                if (username != null && !username.isEmpty()) {
                    taskToUpdate.setUserAssigned(userRepository.findByUserName(username));
                }
                if (categoryName != null && !categoryName.isEmpty()) {
                    TaskCategory taskCategory = getTaskCategory(categoryName);
                    taskToUpdate.setTaskCategory(taskCategory);
                }
                taskRepository.save(taskToUpdate);
                return "SUCCESS";
            }
            return "NOT_ALLOWED_USER";
        }
        return "NOT_FOUND";
    }

    /**
     * Updates every data of a Task store in the Database.
     *
     * @param id           the id of the task
     * @param user         the current user of the application
     * @param name         the new name of the task
     * @param description  the new description of the task
     * @param deadline     the new deadline of the task
     * @param username     the new user assigned to the task
     * @param categoryName the new category of the task
     * @return a String according to the outcome of the update.
     */
    @Override
    public String completeUpdateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task taskToUpdate = task.get();
            // If the user is an Admin OR if he is the one assigned to the task
            if (user.isAdmin() || taskToUpdate.getUserAssigned().getUsername().equals(user.getUsername())) {
                // The user can modify the Task
                taskToUpdate.setTaskName(name);
                taskToUpdate.setTaskDescription(description);
                taskToUpdate.setDeadline(Timestamp.valueOf(deadline));
                taskToUpdate.setUserAssigned(userRepository.findByUserName(username));

                TaskCategory taskCategory = getTaskCategory(categoryName);
                taskToUpdate.setTaskCategory(taskCategory);

                taskRepository.save(taskToUpdate);
                return "SUCCESS";
            }
            return "NOT_ALLOWED_USER";
        }
        return "NOT_FOUND";
    }


}
