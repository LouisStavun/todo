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
    private String categoryName;

    @Column(length = 500)
    private String categoryDescription;

    public TaskCategory(String category_name, String categoryDescription) {
        this.categoryName = category_name;
        this.categoryDescription = categoryDescription;
    }

    public TaskCategory() {

    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String category_name) {
        this.categoryName = category_name;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String category_description) {
        this.categoryDescription = category_description;
    }
}
