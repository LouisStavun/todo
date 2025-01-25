package ch.cern.todo.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer task_id;

    @Column(length = 100, nullable = false)
    private String taskName;

    @Column(length = 500)
    private String taskDescription;

    @Column(nullable = false)
    private Timestamp deadline;


    @ManyToOne()
    @JoinColumn(name = "category_id")
    private TaskCategory taskCategory;

    @ManyToOne()
    @JoinColumn(name = "userID_assigned")
    private UserApp userAssigned;

    public Task(String taskName, String taskDescription, Timestamp deadline,
                TaskCategory taskCategory, UserApp user) {
        super();
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.deadline = deadline;
        this.taskCategory = taskCategory;
        this.userAssigned = user;
    }

    public Task() {}



    public String getTaskName() {
        return taskName;
    }
    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public TaskCategory getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(TaskCategory taskCategory) {
        this.taskCategory = taskCategory;
    }

    public UserApp getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(UserApp userAssigned) {
        this.userAssigned = userAssigned;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public Timestamp getDeadline() {
        return deadline;
    }
}
