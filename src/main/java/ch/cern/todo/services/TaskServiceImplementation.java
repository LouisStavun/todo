package ch.cern.todo.services;

import ch.cern.todo.exceptions.TaskNotFoundException;
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
    public Task getById(int task_id) throws TaskNotFoundException {
        return taskRepository.findById(task_id)
                .orElseThrow(() -> new TaskNotFoundException("Task not existing"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves Task Category by name, if it doesn't exist, creates it.
     *
     * @param taskCategoryName
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
     * @param taskName
     * @param taskDescription
     * @param deadline
     * @param taskCategoryName
     * @param user
     * @return the created Task
     */
    public Task createTask(String taskName, String taskDescription, Timestamp deadline,
                           String taskCategoryName, UserApp user) {

        TaskCategory taskCategory = getTaskCategory(taskCategoryName);
        Task task = new Task(taskName, taskDescription, deadline, taskCategory, user);
        return taskRepository.save(task);
    }


    /**
     * Retrieves Tasks corresponding to all the argument stored in the Database.
     *
     * @param user
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
     * @return 1 if deleted successfully, 0 otherwise.
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
     * @param id
     * @param currentUser
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
     * @param id
     * @param user
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
     * @return the updated Task.
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
     * @param id
     * @param user
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
     * @return
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
