package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.specification.SearchSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

public interface SearchService {

    public List<Task> searchTasks(String name, String description, Timestamp deadline, String username, String categoryName);
}
