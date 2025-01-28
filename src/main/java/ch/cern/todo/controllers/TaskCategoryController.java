package ch.cern.todo.controllers;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class TaskCategoryController {


    @Autowired
    private TaskCategoryService taskCategoryService;

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
     * Creates and saves a Task Category in the Database.
     *
     * @param categoryName
     * @param categoryDescription
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
     * Retrieves a Task Category stored in the Database by its name and deletes it.
     *
     * @param categoryName
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTaskCategoryByName(
            @RequestParam String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        String result = taskCategoryService.deleteTaskCategoryByName(categoryName, currentUser);

        return switch (result) {
            case "SUCCESS" -> ResponseEntity.ok("Category successfully deleted !");
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
            case "NOT_ADMIN" ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have the required permissions to delete this category.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        };
    }

    /**
     * Retrieves a Task Category stored in the Database by its ID and deletes it.
     *
     * @param id
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
     * @param id
     * @param categoryName
     * @param categoryDescription
     * @return the modified Task Category
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
