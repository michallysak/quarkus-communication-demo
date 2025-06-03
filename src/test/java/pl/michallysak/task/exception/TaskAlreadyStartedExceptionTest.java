package pl.michallysak.task.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskAlreadyStartedExceptionTest {

    private final static UUID TASK_ID = UUID.randomUUID();

    @Test
    void testExceptionMessage() {
        // given
        TaskAlreadyStartedException exception = new TaskAlreadyStartedException(TASK_ID);

        // when
        String message = exception.getMessage();

        // then
        assertEquals("Task with id " + TASK_ID + " is already started or completed.", message);
    }
}