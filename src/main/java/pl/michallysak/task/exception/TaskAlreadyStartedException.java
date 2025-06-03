package pl.michallysak.task.exception;

import java.util.UUID;

public class TaskAlreadyStartedException extends RuntimeException {
    public TaskAlreadyStartedException(UUID id) {
        super("Task with id " + id + " is already started or completed.");
    }
}
