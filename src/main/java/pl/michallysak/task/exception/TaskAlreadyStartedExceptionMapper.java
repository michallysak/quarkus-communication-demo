package pl.michallysak.task.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TaskAlreadyStartedExceptionMapper implements ExceptionMapper<TaskAlreadyStartedException> {

    @Override
    public Response toResponse(TaskAlreadyStartedException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }
}
