package ch.cern.todo.exceptions;

import java.io.Serial;

public class TaskCategoryNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TaskCategoryNotFoundException() {
        super();
    }

    public TaskCategoryNotFoundException(String message) {
        super(message);
    }
}
