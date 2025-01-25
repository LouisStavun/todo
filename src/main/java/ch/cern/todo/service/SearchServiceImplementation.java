package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.repository.SearchRepository;
import ch.cern.todo.specification.SearchSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SearchServiceImplementation implements SearchService {

    @Autowired
    private SearchRepository searchRepository;

    public List<Task> searchTasks(String name, String description, Timestamp deadline, String username, String categoryName) {
        Specification<Task> spec = SearchSpecification.filterTasks(name, description, deadline, username, categoryName);
        return searchRepository.findAll(spec);
    }
}
