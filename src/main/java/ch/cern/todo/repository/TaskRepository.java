package ch.cern.todo.repository;

import ch.cern.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // Exemple de méthode pour une requête SQL personnalisée
    @Query("SELECT t FROM Task t WHERE t.taskName = :name")
    List<Task> findByName(@Param("name") String name);
}
