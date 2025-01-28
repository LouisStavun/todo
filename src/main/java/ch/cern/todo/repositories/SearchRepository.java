package ch.cern.todo.repositories;

import ch.cern.todo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SearchRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
}
