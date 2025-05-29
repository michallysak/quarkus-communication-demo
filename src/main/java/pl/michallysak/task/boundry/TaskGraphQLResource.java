package pl.michallysak.task.boundry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import pl.michallysak.task.control.TaskController;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.TaskDto;
import pl.michallysak.task.exception.TaskNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@GraphQLApi
@ApplicationScoped
public class TaskGraphQLResource {

    @Inject
    TaskController taskController;

    @Query("allTasks")
    @Description("Get all tasks")
    public List<TaskDto> getAllTasks() {
        return taskController.getTasks();
    }

    @Query
    @Description("Get a task by its ID")
    public TaskDto taskById(UUID id) {
        return runCatching(() -> taskController.getTaskDtoById(id))
            .orElse(null);
    }

    @Mutation
    @Description("Create a new task")
    public TaskDto createTask(@Valid TaskCreateDto taskCreateDto) {
        return taskController.create(taskCreateDto);
    }

    @Mutation
    @Description("Update an existing task")
    public TaskDto updateTask(UUID id, @Valid TaskDto taskDto) {
        return runCatching(() -> taskController.update(id, taskDto))
            .orElse(null);
    }

    @Mutation
    @Description("Delete a task by its ID")
    public boolean deleteTask(UUID id) {
        return runCatching(() -> {
            taskController.delete(id);
            return 0;
        }).isPresent();
    }

    private <T> Optional<T> runCatching(Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (TaskNotFoundException e) {
            return Optional.empty();
        }
    }
}

