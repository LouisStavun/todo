package ch.cern.todo.service;

import ch.cern.todo.exception.TaskNotFoundException;
import ch.cern.todo.model.Task;
import org.springframework.stereotype.Service;
import ch.cern.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class TaskServiceImplementation implements TaskService{

    TaskRepository taskRepository;

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getById(int task_id) throws TaskNotFoundException {
        return taskRepository.findById(task_id)
                .orElseThrow(() -> new TaskNotFoundException("Task not existing"));
    }

    @Override
    public List<Task> getTasksByName(String name) {
        return taskRepository.findByName(name);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public String toString(){
        String ret = "";

        ret = this.getAllTasks().toString();
        return ret;
    }


}
