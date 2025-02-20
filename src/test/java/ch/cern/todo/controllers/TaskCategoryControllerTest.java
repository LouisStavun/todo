package ch.cern.todo.controllers;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.TaskCategoryRepository;
import ch.cern.todo.services.TaskCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskCategoryService taskCategoryService;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

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

    @Test
    void searchCategoryByID_Found() {
        // Arrange
        int categoryId = 1;
        TaskCategory mockCategory = new TaskCategory();
        mockCategory.setCategory_id(categoryId);
        mockCategory.setCategoryName("Sample Category");

        when(taskCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        // Act
        ResponseEntity<Object> response = taskCategoryController.searchCategoryByID(categoryId);

        // Assert
        assertNotNull(response);  // Checks Category is not null
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Checks HTTP status 200
        TaskCategory returnedCategory = (TaskCategory) response.getBody();
        assertNotNull(returnedCategory);  // Checks body is not null and contains a Category
        assertEquals(categoryId, returnedCategory.getCategory_id());  // Checks Category ID
        assertEquals("Sample Category", returnedCategory.getCategoryName());
    }

    @Test
    void searchCategoryByID_NotEqual() {
        // Arrange
        int categoryId = 1;
        int secondCategoryId = 2;
        TaskCategory mockCategory = new TaskCategory();
        mockCategory.setCategory_id(categoryId);
        mockCategory.setCategoryName("Sample Category");

        TaskCategory mockCategory2 = new TaskCategory();
        mockCategory2.setCategory_id(secondCategoryId);
        mockCategory2.setCategoryName("Sample Category 2");

        when(taskCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));
        when(taskCategoryRepository.findById(secondCategoryId)).thenReturn(Optional.of(mockCategory2));

        // Act
        ResponseEntity<Object> response = taskCategoryController.searchCategoryByID(secondCategoryId);

        // Assert
        assertNotNull(response);  // Checks Category is not null
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Checks HTTP status 200
        TaskCategory returnedCategory = (TaskCategory) response.getBody();
        assertNotNull(returnedCategory);  // Checks body is not null and contains a Category
        assertNotEquals(categoryId, returnedCategory.getCategory_id());  // Checks that Category ID is not the one we want
        assertNotEquals("Sample Category", returnedCategory.getCategoryName());
    }

    @Test
    void searchCategoryByID_NotFound() {
        // Arrange
        int categoryId = 1;
        int secondCategoryId = 2;
        TaskCategory mockCategory = new TaskCategory();
        mockCategory.setCategory_id(categoryId);
        mockCategory.setCategoryName("Sample Category");


        when(taskCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        // Act
        ResponseEntity<Object> response = taskCategoryController.searchCategoryByID(secondCategoryId);

        // Assert
        assertNotNull(response);  // Checks Category is not null
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());  // Checks HTTP status 404

    }

    @Test
    void searchCategoryByName_Found() {
        // Arrange
        int categoryId = 1;
        String CategoryName = "Sample Category";
        TaskCategory mockCategory = new TaskCategory();
        mockCategory.setCategory_id(categoryId);
        mockCategory.setCategoryName(CategoryName);

        when(taskCategoryRepository.findByCategoryName(CategoryName)).thenReturn(mockCategory);

        // Act
        TaskCategory response = taskCategoryController.searchCategory(CategoryName);

        // Assert
        assertNotNull(response);  // Checks body is not null and contains a Category
        assertEquals(categoryId, response.getCategory_id());  // Checks Category ID
        assertEquals(CategoryName, response.getCategoryName()); // Checks Category Name
    }

    @Test
    void searchCategoryByName_NotEqual() {
        // Arrange
        int categoryId = 1;
        String CategoryName = "Sample Category";
        TaskCategory mockCategory = new TaskCategory();
        mockCategory.setCategory_id(categoryId);
        mockCategory.setCategoryName(CategoryName);

        int categoryId2 = 2;
        String CategoryName2 = "Sample Category 2";
        TaskCategory mockCategory2 = new TaskCategory();
        mockCategory2.setCategory_id(categoryId2);
        mockCategory2.setCategoryName(CategoryName2);

        when(taskCategoryRepository.findByCategoryName(CategoryName)).thenReturn(mockCategory);
        when(taskCategoryRepository.findByCategoryName(CategoryName2)).thenReturn(mockCategory2);

        // Act
        TaskCategory response = taskCategoryController.searchCategory(CategoryName2);

        // Assert
        assertNotNull(response);  // Checks Category is not null
        assertNotEquals(categoryId, response.getCategory_id());  // Checks Category ID not the same
        assertNotEquals(CategoryName, response.getCategoryName()); // Checks Category Name not the same
    }

    @Test
    void searchCategoryByName_NotFound() {
        // Arrange
        int categoryId = 1;
        String CategoryName = "Sample Category";
        String CategoryName2 = "Sample Category 2";
        TaskCategory mockCategory = new TaskCategory();
        mockCategory.setCategory_id(categoryId);
        mockCategory.setCategoryName(CategoryName2);


        when(taskCategoryRepository.findByCategoryName(CategoryName)).thenReturn(mockCategory);

        // Act
        TaskCategory response = taskCategoryController.searchCategory(CategoryName2);

        // Assert
        assertNull(response);  // Checks Task Category returned is NULL

    }

}
