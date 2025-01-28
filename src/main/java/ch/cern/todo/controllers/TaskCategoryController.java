package ch.cern.todo.controllers;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.TaskCategoryRepository;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class TaskCategoryController {


    @Autowired
    private TaskCategoryService taskCategoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    /**
     * Retrieves current User of the application.
     *
     * @return the current User.
     */
    private UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Creates and saves a Task Category in the Database.
     *
     * @param categoryName        the category name to be created
     * @param categoryDescription the category description
     * @return the Task Category created.
     */
    @PostMapping("/create")
    public TaskCategory createTaskCategory(
            @RequestParam String categoryName,
            @RequestParam(required = false) String categoryDescription
    ) {
        return taskCategoryService.createTaskCategory(categoryName, categoryDescription);
    }


    /**
     * Retrieves a Task Category by its name.
     *
     * @param categoryName the Tasks Category name
     * @return the associated Task Category.
     */
    @GetMapping("/search")
    public TaskCategory searchCategory(
            @RequestParam String categoryName) {
        return taskCategoryRepository.findByCategoryName(categoryName);
    }

    /**
     * Retrieves a Task Category by its ID.
     *
     * @param id the Tasks Category ID
     * @return the associated Task Category.
     */
    @GetMapping("/search/{id}")
    public ResponseEntity<Object> searchCategoryByID(
            @PathVariable int id) {

        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(id);
        return taskCategory.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieves a Task Category stored in the Database by its name and deletes it.
     *
     * @param categoryName the category name
     * @return an HTTP code according to the outcome of the request.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTaskCategoryByName(
            @RequestParam String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskCategoryService.deleteTaskCategoryByName(categoryName, currentUser);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Category successfully deleted !");
            case "NOT_ADMIN" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to delete this category.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }

    /**
     * Retrieves a Task Category stored in the Database by its ID and deletes it.
     *
     * @param id the id of the category
     * @return an HTTP code according to the outcome of the request.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTaskCategoryById(
            @PathVariable Integer id
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskCategoryService.deleteTaskCategoryById(id, currentUser);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Category successfully deleted !");
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
            case "NOT_ADMIN" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to delete this category.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }

    /**
     * Updates and saves a Task Category stored in the Database.
     *
     * @param id                  the Task Category id
     * @param categoryName        the Task Category name
     * @param categoryDescription the Task Category description
     * @return an HTTP code according to the outcome of the request.
     */
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Integer id,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String categoryDescription
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskCategoryService.updateTaskCategory(id, currentUser, categoryName, categoryDescription);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Category successfully modified !");
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
            case "NOT_ALLOWED_USER" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to modify this category.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }
}
