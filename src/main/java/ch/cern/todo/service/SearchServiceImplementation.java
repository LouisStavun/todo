package ch.cern.todo.service;

import ch.cern.todo.model.Task;
import ch.cern.todo.model.UserApp;
import ch.cern.todo.repository.SearchRepository;
import ch.cern.todo.repository.TaskCategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import ch.cern.todo.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchServiceImplementation implements SearchService {


    @Autowired
    SearchRepository searchRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    public List<Task> searchTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName) {
        Specification<Task> spec = filterTasks(user, name, description, deadline, username, categoryName);
        return searchRepository.findAll(spec);
    }



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

            if(!userRepository.findByUserName(currentUser.getUsername()).isAdmin()){
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), currentUser.getUsername()));
            }

            // Filtrer par utilisateur assigné
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("userAssigned").get("userName"), username));
            }

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


            // Filtrer par catégorie
            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.join("taskCategory").get("categoryName"), categoryName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Override
    public Long deleteTasks(UserDetails user, String name, String description, String deadline, String username, String categoryName) {
        Specification<Task> spec = filterTasks(user, name, description, deadline, username, categoryName);
        return searchRepository.delete(spec);
    }

    @Override
    public Task updateTask(int id, UserApp user, String name, String description, String deadline, String username, String categoryName) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            Task taskToUpdate = task.get();
            if (user.isAdmin() || taskToUpdate.getUserAssigned().getUserName().equals(user.getUserName())) {
                if (name != null && !name.isEmpty()) {
                    taskToUpdate.setTaskName(name);
                }
                if (description != null && !description.isEmpty()) {
                    taskToUpdate.setTaskDescription(description);
                }
                if (deadline != null && !deadline.isEmpty()) {
                    taskToUpdate.setDeadline(Timestamp.valueOf(deadline));
                }
                if (username != null && !username.isEmpty()) {
                    taskToUpdate.setUserAssigned(userRepository.findByUserName(username));
                }
                if (categoryName != null && !categoryName.isEmpty()) {
                    taskToUpdate.setTaskCategory(taskCategoryRepository.findByCategoryName(categoryName));
                }
                return taskRepository.save(taskToUpdate);
            }
        }
        return null;
    }
}
