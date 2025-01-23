package ch.cern.todo.exceptions;

public class TaskNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public TaskNotFoundException(){
        super();
    }

    public TaskNotFoundException(String message){
        super(message);
    }
}
