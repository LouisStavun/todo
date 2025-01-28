package ch.cern.todo.controllers;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Deletes a Task Category stored in the Database.
     *
     * @param categoryName
     */
    @DeleteMapping("/delete")
    public void deleteTaskCategory(
            @RequestParam String categoryName
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        taskCategoryService.deleteTaskCategory(categoryName, currentUser);
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
    public TaskCategory updateCategory(
            @PathVariable Integer id,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String categoryDescription
    ) {
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskCategoryService.updateTaskCategory(id, currentUser, categoryName, categoryDescription);

    }
}
