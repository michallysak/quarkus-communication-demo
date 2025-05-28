package pl.michallysak.task.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionMapperTest {

    private final static UUID TASK_ID = UUID.randomUUID();
    NotFoundExceptionMapper notFoundExceptionMapper = new NotFoundExceptionMapper();

    @Test
    void testMappingException() {
        // given
        TaskNotFoundException exception = new TaskNotFoundException(TASK_ID);

        // when
        Response response = notFoundExceptionMapper.toResponse(exception);

        // then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Task with id " + TASK_ID + " not found", response.getEntity());
    }
}
