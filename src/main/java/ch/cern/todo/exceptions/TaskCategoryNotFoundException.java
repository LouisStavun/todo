package ch.cern.todo.exceptions;

public class TaskCategoryNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public TaskCategoryNotFoundException(){
        super();
    }

    public TaskCategoryNotFoundException(String message){
        super(message);
    }
}
