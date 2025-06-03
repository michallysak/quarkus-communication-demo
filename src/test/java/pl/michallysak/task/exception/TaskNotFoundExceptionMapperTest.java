package pl.michallysak.task.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskNotFoundExceptionMapperTest {

    private final static UUID TASK_ID = UUID.randomUUID();
    TaskNotFoundExceptionMapper taskNotFoundExceptionMapper = new TaskNotFoundExceptionMapper();

    @Test
    void testMappingException() {
        // given
        TaskNotFoundException exception = new TaskNotFoundException(TASK_ID);

        // when
        Response response = taskNotFoundExceptionMapper.toResponse(exception);

        // then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Task with id " + TASK_ID + " not found", response.getEntity());
    }
}
