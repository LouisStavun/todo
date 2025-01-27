package ch.cern.todo.controller;

import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class TaskCategoryController {


    @Autowired
    private TaskCategoryService taskCategoryService;

    @PostMapping("/create")
    public TaskCategory createTaskCategory(
            @RequestParam String category_name,
            @RequestParam String categoryDescription
    ){
        return taskCategoryService.createTaskCategory(category_name,categoryDescription);
    }
}
