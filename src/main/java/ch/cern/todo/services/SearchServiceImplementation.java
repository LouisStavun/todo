package ch.cern.todo.services;

import ch.cern.todo.models.Task;
import ch.cern.todo.models.UserApp;
import ch.cern.todo.repositories.SearchRepository;
import ch.cern.todo.repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImplementation implements SearchService {


    @Autowired
    SearchRepository searchRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Searches every task corresponding to all the arguments
     *
     * @param user         the current Application user
     * @param name         the names of the Tasks
     * @param description  the descriptions of the Tasks
     * @param deadline     the deadlines of the Tasks
     * @param username     the user assigned to the Tasks
     * @param categoryName the category assigned to the Tasks
     * @return the list of Tasks corresponding to all the arguments
     */
    public List<Task> searchTasks(UserApp user, String name, String description, String deadline, String username, String categoryName) {
        Specification<Task> spec = filterTasks(user, name, description, deadline, username, categoryName);
        return searchRepository.findAll(spec);
    }


    /**
     * Filters each argument to get every Task corresponding
     *
     * @param user         the current Application user
     * @param name         the names of the Tasks
     * @param description  the descriptions of the Tasks
     * @param deadline     the deadlines of the Tasks
     * @param username     the user assigned to the Tasks
     * @param categoryName the category assigned to the Tasks
     * @return a Specification of Tasks, containing every Tasks that match every argument
     */
    public Specification<Task> filterTasks(
            UserDetails user,
            String name,
            String description,
            String deadline,
            String username,
            String categoryName
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // If the current user is not an Admin
            if (!userRepository.findByUserName(user.getUsername()).isAdmin()) {
                // We want to be sure that the current user is the one assigned to the task, otherwise he shouldn't have access to the data
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), user.getUsername()));
            }

            // UserAssigned filter
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), username));
            }

            // Task Name filter
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("taskName"), "%" + name + "%"));
            }

            // Task Description filter
            if (description != null && !description.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("taskDescription"), "%" + description + "%"));
            }

            // Deadline filter
            if (deadline != null) {
                predicates.add(criteriaBuilder.equal(root.get("deadline"), Timestamp.valueOf(deadline)));
            }


            // Category Name filter
            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("taskCategory").get("categoryName"), categoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
