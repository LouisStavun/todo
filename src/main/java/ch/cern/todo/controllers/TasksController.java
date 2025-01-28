package ch.cern.todo.controllers;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.TaskRepository;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.SearchService;
import ch.cern.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TasksController {


    @Autowired
    private TaskService taskService;


    @Autowired
    private SearchService searchService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;


    /**
     * Retrieves current User of the application.
     *
     * @return the current User.
     */
    private UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    /**
     * Creates and saves a Task in the Database.
     *
     * @param name         the Task name
     * @param description  the Task description
     * @param deadline     the Task deadline
     * @param categoryName the Task category
     * @return the created Task.
     */
    @PostMapping("/create")
    public Task createTask(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam String deadline,
            @RequestParam String categoryName) {

        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskService.createTask(name, description, Timestamp.valueOf(deadline), categoryName, currentUser);

    }

    /**
     * Searches for every Task matching all the arguments, and returns a list containing them.
     *
     * @param name         the Tasks names
     * @param description  the Tasks descriptions
     * @param deadline     the Tasks deadlines
     * @param username     the Tasks assigned users
     * @param categoryName the Tasks category name
     * @return the associated list of Tasks
     */
    @GetMapping("/search")
    public List<Task> searchTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName) {

        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return searchService.searchTasks(currentUser, name, description, deadline, username, categoryName);
    }

    /**
     * Retrieves a Task by its ID.
     *
     * @param id the Task ID
     * @return the associated Task.
     */
    @GetMapping("/search/{id}")
    public ResponseEntity<Object> searchTaskById(
            @PathVariable int id
    ) {
        Optional<Task> task = taskRepository.findById(id);
        return task.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * Deletes Tasks stored in the Database matching all the arguments.
     *
     * @param name         the Task name
     * @param description  the Task description
     * @param deadline     the Task deadline
     * @param username     the Task assigned user
     * @param categoryName the Task category
     * @return an HTTP code according to the outcome of the request.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName) {

        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskService.deleteTasks(currentUser, name, description, deadline, username, categoryName);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Task successfully deleted !");
            case "FAILURE" ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found or you don't have permission to delete this task.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }


    /**
     * Retrieves a Task stored in the Database by its ID and deletes it.
     *
     * @param id the ID of the Task to be deleted
     * @return an HTTP code according to the outcome of the request.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTaskById(
            @PathVariable Integer id
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskService.deleteTaskById(id, currentUser);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Task successfully deleted !");
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
            case "NOT_ADMIN" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to delete this task.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }

    /**
     * Updates and saves a Task stored in the Database.
     *
     * @param id           the ID of the Task to be deleted
     * @param name         the Task name
     * @param description  the Task description
     * @param deadline     the Task deadline
     * @param username     the Task assigned user
     * @param categoryName the Task category
     * @return an HTTP code according to the outcome of the request.
     */
    @PatchMapping("/partialUpdate/{id}")
    public ResponseEntity<String> updateTask(
            @PathVariable Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskService.partialUpdateTask(id, currentUser, name, description, deadline, username, categoryName);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Task successfully modified !");
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
            case "NOT_ALLOWED_USER" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to modify this task. You have to be an admin or the assigned user.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }

    /**
     * Updates every value of the Task stored in the Database and saves it.
     *
     * @param id           the ID of the Task to be deleted
     * @param name         the Task name
     * @param description  the Task description
     * @param deadline     the Task deadline
     * @param username     the Task assigned user
     * @param categoryName the Task category
     * @return an HTTP code according to the outcome of the request.
     */
    @PutMapping("/completeUpdate/{id}")
    public ResponseEntity<String> updateTaskComplete(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String deadline,
            @RequestParam String username,
            @RequestParam String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskService.completeUpdateTask(id, currentUser, name, description, deadline, username, categoryName);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Task successfully modified !");
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
            case "NOT_ALLOWED_USER" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to modify this task. You have to be an admin or the assigned user.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }
}
