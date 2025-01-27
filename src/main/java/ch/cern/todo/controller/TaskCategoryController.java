package ch.cern.todo.controller;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
            @RequestParam String category_name,
            @RequestParam String categoryDescription
    ){
        return taskCategoryService.createTaskCategory(category_name,categoryDescription);
    }

    @DeleteMapping("/delete")
    public void deleteTaskCategory(
            @RequestParam String category_name
    ){
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        taskCategoryService.deleteTaskCategory(category_name,currentUser);
    }
}
