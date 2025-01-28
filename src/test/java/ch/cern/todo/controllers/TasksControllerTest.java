package ch.cern.todo.controllers;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

}