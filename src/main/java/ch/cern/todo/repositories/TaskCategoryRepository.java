package ch.cern.todo.repositories;

import ch.cern.todo.models.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {

    TaskCategory findByCategoryName(String categoryName);
}
