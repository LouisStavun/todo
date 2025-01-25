package ch.cern.todo.controller;

import ch.cern.todo.model.Task;
import ch.cern.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DataBaseController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/home")
    public String listTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks(); // Récupération des tâches
        model.addAttribute("tasks", tasks); // Ajout au modèle
        return "task"; // Correspond au fichier task.html
    }

    @GetMapping("/try")
    public String thymeleafTest() {
        return "test";
    }

}
