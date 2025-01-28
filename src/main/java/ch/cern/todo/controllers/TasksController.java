package ch.cern.todo.controllers;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.SearchService;
import ch.cern.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {


    @Autowired
    private TaskService taskService;


    @Autowired
    private SearchService searchService;

    @Autowired
    private UserRepository userRepository;


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
     * @param name
     * @param description
     * @param deadline
     * @param categoryName
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
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
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
     * Deletes Tasks stored in the Database matching all the arguments.
     *
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
     * @return 1 if the Task has been successfully deleted, O otherwise.
     */
    @DeleteMapping("/delete")
    public Long deleteTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName) {

        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskService.deleteTasks(currentUser, name, description, deadline, username, categoryName);
    }


    /**
     * Retrieves a Task stored in the Database by its ID and deletes it.
     * @param id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteTaskById(
            @PathVariable Integer id
    ){
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        taskService.deleteTaskById(id,currentUser);
    }

    /**
     * Updates and saves a Task stored in the Database.
     *
     * @param id
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
     * @return the Task updated.
     */
    @PatchMapping("/partialUpdate/{id}")
    public Task updateTask(
            @PathVariable Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskService.partialUpdateTask(id, currentUser, name, description, deadline, username, categoryName);
    }

    @PutMapping("/completeUpdate/{id}")
    public Task updateTaskComplete(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String deadline,
            @RequestParam String username,
            @RequestParam String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskService.completeUpdateTask(id, currentUser, name, description, deadline, username, categoryName);
    }
}
