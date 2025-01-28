package ch.cern.todo.controllers;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.UserRepository;
import ch.cern.todo.services.TaskCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskCategoryService taskCategoryService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskCategoryController taskCategoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserDetails userDetails = User.withUsername("testUser")
                .password("password")
                .roles("ADMIN")
                .build();


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());


        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateTaskCategory() {
        // Arrange
        String name = "Test Task";
        String description = "Test Description";

        UserApp user = new UserApp();
        user.setUserName("testUser");

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setCategory_id(1);
        taskCategory.setCategoryName(name);
        taskCategory.setCategoryDescription(description);

        // Mock dependencies
        when(taskCategoryService.createTaskCategory(name, description)).thenReturn(taskCategory);

        // Act
        TaskCategory createdTaskCategory = taskCategoryController.createTaskCategory(name, description);

        // Assert
        assertNotNull(createdTaskCategory);  // Checks Category created is not null
        assertEquals(name, createdTaskCategory.getCategoryName());  // Checks name is correct
        assertEquals(description, createdTaskCategory.getCategoryDescription());  // Checks description is correct
    }

}
