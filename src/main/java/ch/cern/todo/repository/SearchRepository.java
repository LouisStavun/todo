package ch.cern.todo.repository;

import ch.cern.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SearchRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
}
