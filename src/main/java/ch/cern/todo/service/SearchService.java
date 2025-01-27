package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

public interface SearchService {

    public List<Task> searchTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName);
}
