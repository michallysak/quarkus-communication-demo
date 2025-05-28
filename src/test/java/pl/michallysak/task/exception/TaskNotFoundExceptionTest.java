package pl.michallysak.task.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskNotFoundExceptionTest {

    private final static UUID TASK_ID = UUID.randomUUID();

    @Test
    void testExceptionMessage() {
        // given
        TaskNotFoundException taskNotFoundException = new TaskNotFoundException(TASK_ID);

        // when
        String message = taskNotFoundException.getMessage();

        // then
        assertEquals("Task with id " + TASK_ID + " not found", message);
    }
}
