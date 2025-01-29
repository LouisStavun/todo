package ch.cern.todo.controllers;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.TaskRepository;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TasksController tasksController;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserDetails userDetails = User.withUsername("testUser")
                .password("password")
                .roles("USER")
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    /**
     * Tests to create a Task with the good User assigned
     */
    @Test
    void testCreateTaskUserWellAssigned() {
        // Arrange
        String name = "Test Task";
        String description = "Test Description";
        String deadline = "2025-01-30 12:00:00";
        String categoryName = "Work";

        UserApp user = new UserApp();
        user.setUserName("testUser");

        Task task = new Task();
        task.setTaskName(name);
        task.setTaskDescription(description);
        task.setDeadline(Timestamp.valueOf(deadline));

        // Mock dependencies
        when(userRepository.findByUserName("testUser")).thenReturn(user);
        when(taskService.createTask(name, description, Timestamp.valueOf(deadline), categoryName, user)).thenReturn(task);

        // Act
        Task createdTask = tasksController.createTask(name, description, deadline, categoryName);

        // Assert
        assertNotNull(createdTask);  // Checks created Task is not null
        assertEquals(name, createdTask.getTaskName());  // Checks name is correct
        assertEquals(description, createdTask.getTaskDescription());  // Check description is correct
        assertEquals(Timestamp.valueOf(deadline), createdTask.getDeadline());  // Checks deadline is correct
    }

    @Test
    void testSearchTaskById() {
        // Arrange
        int taskId = 1;
        Task task = new Task();
        task.setTask_id(taskId);
        task.setTaskName("Test Task");

        // Mock the repository call
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        ResponseEntity<Object> response = tasksController.searchTaskById(taskId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Checks HTTP status 200
        assertNotNull(response.getBody());  // Checks body is not null
        Task returnedTask = (Task) response.getBody();
        assertEquals("Test Task", returnedTask.getTaskName());  // Checks task name is correct
    }

    @Test
    void testSearchTaskByIdNotEqual() {
        // Arrange
        int taskId = 1;
        Task task = new Task();
        task.setTask_id(taskId);
        task.setTaskName("Test Task");

        int taskId2 = 2;
        Task task2 = new Task();
        task2.setTask_id(taskId2);
        task2.setTaskName("Test Task 2");

        // Mock the repository call
        when(taskRepository.findById(taskId2)).thenReturn(Optional.of(task2));

        // Act
        ResponseEntity<Object> response = tasksController.searchTaskById(taskId2);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Checks HTTP status 200
        assertNotNull(response.getBody());  // Checks body is not null
        Task returnedTask = (Task) response.getBody();
        assertNotEquals("Test Task", returnedTask.getTaskName());  // Checks task name is not correct
    }

    @Test
    void testSearchTaskByIdNotFound() {
        // Arrange
        int taskId = 1;
        int taskId2 = 2;

        Task task = new Task();
        task.setTask_id(taskId);
        task.setTaskName("Test Task");


        // Mock the repository call
        when(taskRepository.findById(taskId2)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = tasksController.searchTaskById(taskId2);

        // Assert
        assertNotNull(response);  // Checks Category is not null
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());  // Checks HTTP status 404
    }


}