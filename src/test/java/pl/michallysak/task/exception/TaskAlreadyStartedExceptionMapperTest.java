package pl.michallysak.task.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskAlreadyStartedExceptionMapperTest {

    private final static UUID TASK_ID = UUID.randomUUID();
    TaskAlreadyStartedExceptionMapper mapper = new TaskAlreadyStartedExceptionMapper();

    @Test
    void testMappingException() {
        // given
        TaskAlreadyStartedException exception = new TaskAlreadyStartedException(TASK_ID);

        // when
        Response response = mapper.toResponse(exception);

        // then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Task with id " + TASK_ID + " is already started or completed.", response.getEntity());
    }
}