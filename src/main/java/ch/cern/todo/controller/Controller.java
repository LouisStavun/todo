package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.service.SearchService;
import ch.cern.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class Controller {


    @Autowired
    private TaskService taskService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/api/tasks")
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }


    @GetMapping("/pi")
    public String getString() {
        return "Hello World";
    }

    @GetMapping("/hame")
    public String homePage() {
        return "Welcome to the Home Page";
    }

    @GetMapping("/api/{resourceId}")
    public String receiveData (@PathVariable String resourceId) {
        return "Resource Id: " + resourceId;
    }

    @GetMapping("/delete")
    public String delete()
    {
        return "This is the delete request";
    }

//    // Endpoint pour récupérer des tâches par nom
//    @GetMapping("/tasks/search")
//    public List<Task> getTasksByName(@RequestParam String name) {
//        return taskService.getTasksByName(name);
//    }

    @GetMapping("/tasks/search")
    public List<Task> searchTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String categoryName) {

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //((UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRole();
        //System.out.println(currentUsername);
        return searchService.searchTasks(currentUser, name, description, deadline, username, categoryName);
    }

}
