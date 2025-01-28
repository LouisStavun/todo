package ch.cern.todo.services;

import ch.cern.todo.models.Task;
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
     * @param user
     * @param name
     * @param description
     * @param deadline
     * @param username
     * @param categoryName
     * @return the list of Tasks corresponding to all the arguments
     */
    public List<Task> searchTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName) {
        Specification<Task> spec = filterTasks(user, name, description, deadline, username, categoryName);
        return searchRepository.findAll(spec);
    }


    /**
     * Filters each argument to get every Task corresponding
     *
     * @param currentUser
     * @param taskName
     * @param taskDescription
     * @param taskDeadline
     * @param username
     * @param categoryName
     * @return a Specification of Tasks, containing every Tasks that match every argument
     */
    public Specification<Task> filterTasks(
            UserDetails currentUser,
            String taskName,
            String taskDescription,
            String taskDeadline,
            String username,
            String categoryName
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // If the current user is not an Admin
            if (!userRepository.findByUserName(currentUser.getUsername()).isAdmin()) {
                // We want to be sure that the current user is the one assigned to the task, otherwise he shouldn't have access to the data
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), currentUser.getUsername()));
            }

            // UserAssigned filter
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), username));
            }

            // Task Name filter
            if (taskName != null && !taskName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }

            // Task Description filter
            if (taskDescription != null && !taskDescription.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("taskDescription"), "%" + taskDescription + "%"));
            }

            // Deadline filter
            if (taskDeadline != null) {
                predicates.add(criteriaBuilder.equal(root.get("deadline"), Timestamp.valueOf(taskDeadline)));
            }


            // Category Name filter
            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("taskCategory").get("categoryName"), categoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
