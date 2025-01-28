package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SearchService {

    List<Task> searchTasks(UserApp user, String name, String description, String deadline, String username, String categoryName);

    Specification<Task> filterTasks(UserDetails currentUser, String taskName, String taskDescription, String taskDeadline, String username, String categoryName);

}
