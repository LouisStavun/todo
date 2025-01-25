package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {


    @Autowired
    private TaskService taskService;

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
}
