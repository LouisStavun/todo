package ch.cern.todo;

import ch.cern.todo.exceptions.TaskNotFoundException;
import ch.cern.todo.model.Task;
import ch.cern.todo.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import ch.cern.todo.service.TaskService;

import java.sql.Timestamp;
import java.time.Instant;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TodoApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(TodoApplication.class, args);
	}

	@Autowired
	TaskService taskService;

	@Autowired
	TaskCategoryService taskCategoryService;

	@Override
	public void run(String... args) throws TaskNotFoundException {
		TaskCategory taskCategory1 = new TaskCategory("Category 1","");
		TaskCategory taskCategory2 = new TaskCategory("Category 2","");
		TaskCategory taskCategory3 = new TaskCategory("Category 3","This is a Task Category");
		TaskCategory taskCategory4 = new TaskCategory("Category 4","");

		Task task1 = new Task("Task 1","This is a Task", Timestamp.from(Instant.now()),taskCategory1);
		Task task2 = new Task("Task 2","", Timestamp.valueOf("2025-01-23 17:27:34.59174"),taskCategory1);
		Task task3 = new Task("Task 3","", Timestamp.valueOf("2025-01-23 17:49:14.813298"),taskCategory2);
		Task task4 = new Task("Task 4","", Timestamp.from(Instant.now()),taskCategory3);
		Task task5 = new Task("Task 5","This is another Task", Timestamp.from(Instant.now()),taskCategory3);

		taskCategoryService.addTaskCategory(taskCategory1);
		taskCategoryService.addTaskCategory(taskCategory2);
		taskCategoryService.addTaskCategory(taskCategory3);
		taskCategoryService.addTaskCategory(taskCategory4);

		taskService.addTask(task1);
		taskService.addTask(task2);
		taskService.addTask(task3);
		taskService.addTask(task4);
		taskService.addTask(task5);
	}
}
