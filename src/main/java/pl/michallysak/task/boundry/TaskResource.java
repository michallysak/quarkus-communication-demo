package pl.michallysak.task.boundry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.michallysak.task.control.TaskController;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.TaskDto;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TaskResource {

    @Inject
    TaskController taskController;

    @GET
    public List<TaskDto> get() {
        return taskController.getTasks();
    }

    @GET
    @Path("/{id}")
    public TaskDto getById(@PathParam("id") UUID uuid) {
        return taskController.getTaskDtoById(uuid);
    }

    @POST
    public Response create(@Valid TaskCreateDto createTaskDto) {
        TaskDto taskDto = taskController.create(createTaskDto);
        URI location = URI.create("/task/%s".formatted(taskDto.getId()));
        return Response.created(location)
            .entity(taskDto)
            .build();
    }

    @PUT
    @Path("/{id}")
    public TaskDto update(@PathParam("id") UUID uuid, @Valid TaskDto taskDto) {
        return taskController.update(uuid, taskDto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") UUID uuid) {
        taskController.delete(uuid);
    }

    @DELETE
    public void deleteAll() {
        taskController.deleteAll();
    }

}
