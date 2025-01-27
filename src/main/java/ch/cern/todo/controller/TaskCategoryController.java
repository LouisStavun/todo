package ch.cern.todo.controller;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.service.TaskCategoryService;
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

    private UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/create")
    public TaskCategory createTaskCategory(
            @RequestParam String categoryName,
            @RequestParam String categoryDescription
    ){
        return taskCategoryService.createTaskCategory(categoryName,categoryDescription);
    }

    @DeleteMapping("/delete")
    public void deleteTaskCategory(
            @RequestParam String categoryName
    ){
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        taskCategoryService.deleteTaskCategory(categoryName,currentUser);
    }

    @PatchMapping("/update/{id}")
    public TaskCategory updateCategory(
            @PathVariable Integer id,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String categoryDescription
    ){
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskCategoryService.updateTaskCategory(id,currentUser, categoryName, categoryDescription);

    }
}
