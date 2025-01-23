package ch.cern.todo.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer task_id;

    @Column(length = 100, nullable = false)
    private String task_name;

    @Column(length = 500)
    private String task_description;

    @Column(nullable = false)
    private Timestamp deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TaskCategory taskCategory;

    public Task(String task_name, String task_description, Timestamp deadline, TaskCategory taskCategory) {
        super();
        this.task_name = task_name;
        this.task_description = task_description;
        this.deadline = deadline;
        this.taskCategory = taskCategory;
    }

    public Task() {}

    public Integer getTask_id() {
        return task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_description() {
        return task_description;
    }

    public Timestamp getDeadline() {
        return deadline;
    }
}
