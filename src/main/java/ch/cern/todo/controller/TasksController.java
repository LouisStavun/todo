package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.UserRepository;
import ch.cern.todo.service.SearchService;
import ch.cern.todo.service.TaskService;
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

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;


    private UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @PostMapping("/create")
    public Task createTask(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam String deadline,
            @RequestParam String categoryName) {

        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return taskService.createTask(name,description,Timestamp.valueOf(deadline),categoryName,currentUser);

    }



    @GetMapping("/search")
    public List<Task> searchTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName) {

        UserDetails currentUser = this.getCurrentUser();
        return searchService.searchTasks(currentUser, name, description, deadline, username, categoryName);
    }


    @DeleteMapping("/delete")
    public Long deleteTask(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName) {

        UserDetails currentUser = this.getCurrentUser();
        return searchService.deleteTasks(currentUser, name, description, deadline, username, categoryName);
    }

    @PatchMapping("/update/{id}")
    public Task updateTask(
            @PathVariable Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName
    ){
        UserApp currentUser = userRepository.findByUserName(this.getCurrentUser().getUsername());
        return searchService.updateTask(id,currentUser, name, description, deadline, username, categoryName);

    }

}
