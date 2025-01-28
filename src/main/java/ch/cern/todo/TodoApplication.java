package ch.cern.todo;

import ch.cern.todo.enums.Role;
import ch.cern.todo.exceptions.TaskNotFoundException;
import ch.cern.todo.models.Task;
import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.TaskCategoryService;
import ch.cern.todo.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;

@SpringBootApplication()
public class TodoApplication implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication.run(TodoApplication.class, args);
    }

    @Autowired
    TaskService taskService;

    @Autowired
    TaskCategoryService taskCategoryService;

    UserApp userApp1 = null;
    UserApp userApp2 = null;
    UserApp userApp3 = null;
    UserApp userApp4 = null;


    /**
     * Load Users and encodes their password into the Database. Done in first to avoid User assignment issues.
     *
     * @param userRepository
     * @param passwordEncoder
     * @return
     */
    @Bean
    @Order(1)
    public CommandLineRunner loadData(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userApp1 = new UserApp("admin1", passwordEncoder.encode("admin"), Role.ADMIN);
                userApp2 = new UserApp("admin2", passwordEncoder.encode("admin"), Role.ADMIN);
                userApp3 = new UserApp("user1", passwordEncoder.encode("user"), Role.USER);
                userApp4 = new UserApp("user2", passwordEncoder.encode("user"), Role.USER);
                userRepository.save(userApp1);
                userRepository.save(userApp2);
                userRepository.save(userApp3);
                userRepository.save(userApp4);
            }
        };
    }

    @Override
    public void run(String... args) throws TaskNotFoundException {


        TaskCategory taskCategory1 = new TaskCategory("Category 1", "");
        TaskCategory taskCategory2 = new TaskCategory("Category 2", "");
        TaskCategory taskCategory3 = new TaskCategory("Category 3", "This is a Task Category");
        TaskCategory taskCategory4 = new TaskCategory("Category 4", "");

        Task task1 = new Task("Task 1", "This is a Task", Timestamp.from(Instant.now()), taskCategory1, userApp1);
        Task task2 = new Task("Task 2", "", Timestamp.valueOf("2025-01-23 17:27:34.59174"), taskCategory1, userApp2);
        Task task3 = new Task("Task 3", "", Timestamp.valueOf("2025-01-23 17:49:14.813298"), taskCategory2, userApp3);
        Task task4 = new Task("Task 4", "", Timestamp.valueOf("2025-01-24 14:17:48.50652"), taskCategory3, userApp3);
        Task task5 = new Task("Task 5", "This is another Task", Timestamp.valueOf("2025-01-25 21:35:47.045261"), taskCategory3, null);


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
