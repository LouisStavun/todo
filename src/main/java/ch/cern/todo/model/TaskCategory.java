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

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }
}
