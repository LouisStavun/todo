package ch.cern.todo.specification;

import ch.cern.todo.model.Task;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class SearchSpecification {
    public static Specification<Task> filterTasks(
            String taskName,
            String taskDescription,
            String taskDeadline, // Mettre un String et cast to Timestamp
            String username,
            String categoryName
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrer par nom de tâche
            if (taskName != null && !taskName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }

            // Filtrer par description
            if (taskDescription != null && !taskDescription.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("taskDescription"), "%" + taskDescription + "%"));
            }

            // Filtrer par deadline
            if (taskDeadline != null) {
                predicates.add(criteriaBuilder.equal(root.get("deadline"), Timestamp.valueOf(taskDeadline)));
            }

            // Filtrer par utilisateur assigné
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), username));
            }

            // Filtrer par catégorie
            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("taskCategory").get("categoryName"), categoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
