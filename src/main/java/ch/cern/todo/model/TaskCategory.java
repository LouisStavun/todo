package ch.cern.todo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class TaskCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer category_id;

    @Column(length = 100, nullable = false, unique = true)
    private String category_name;

    @Column(length = 500)
    private String category_description;

    public TaskCategory(String category_name, String category_description) {
        this.category_name = category_name;
        this.category_description = category_description;
    }

    public TaskCategory() {

    }
}
