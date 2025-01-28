package ch.cern.todo.controllers;

import ch.cern.todo.models.Task;
import ch.cern.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DataBaseController {

    @Autowired
    private TaskService taskService;

    /**
     * Displays Tasks on http://localhost:8080/home, from task.html file
     *
     * @param model
     */
    @GetMapping("/home")
    public String listTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "task";
    }

}
