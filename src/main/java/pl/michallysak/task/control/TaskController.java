package pl.michallysak.task.control;

import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.Response;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.TaskDto;
import pl.michallysak.task.domain.TaskStatus;
import pl.michallysak.task.exception.TaskAlreadyStartedException;
import pl.michallysak.task.exception.TaskNotFoundException;
import pl.michallysak.task.mapper.TaskMapper;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TaskController {

    private final static long LONG_POLL_TIMEOUT = 9000;
    public final static int TASK_STATUS_CHECK_INTERVAL = 500;

    @Inject
    private Vertx vertx;

    @Inject
    private TaskMapper taskMapper;

    public void update(Task task) {
        Task.update(task);
    }

    public List<TaskDto> getTasks() {
        return listAll().stream()
            .map(taskMapper::toDto)
            .toList();
    }

    public TaskDto getTaskDtoById(UUID uuid) {
        Task task = getTaskById(uuid);
        return taskMapper.toDto(task);
    }

    public TaskDto create(TaskCreateDto taskDto) {
        Task task = taskMapper.fromCreateDto(taskDto);
        task.persist();
        return taskMapper.toDto(task);
    }

    public TaskDto update(UUID uuid, TaskDto taskDto) {
        Task existingTask = getTaskById(uuid);
        existingTask.setName(taskDto.getName());
        update(existingTask);
        return taskMapper.toDto(existingTask);
    }

    public void delete(UUID uuid) {
        Task task = getTaskById(uuid);
        task.delete();
    }

    public void deleteAll() {
        Task.deleteAll();
    }

    public void process(UUID id) {
        Task task = getTaskById(id);
        if (task.getStatus() == TaskStatus.IN_PROGRESS) {
            throw new TaskAlreadyStartedException(id);
        }

        task.setStatus(TaskStatus.IN_PROGRESS);
        update(task);
        CompletableFuture.runAsync(() -> {
            try {
                int randomProcessTime = getRandomProcessTime();
                Thread.sleep(randomProcessTime);
                task.setStatus(TaskStatus.COMPLETED);
                update(task);
            } catch (InterruptedException e) {
                task.setStatus(TaskStatus.CREATED);
                update(task);
            }
        });
    }

    public void getByIdWithLongPoll(UUID id, AsyncResponse asyncResponse) {
        final long start = System.currentTimeMillis();

        Runnable check = new Runnable() {
            public void run() {
                TaskDto task = getTaskDtoById(id);
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    Response response = Response.ok(task).build();
                    asyncResponse.resume(response);
                } else if (System.currentTimeMillis() - start > LONG_POLL_TIMEOUT) {
                    Response response = Response.status(Response.Status.NO_CONTENT).build();
                    asyncResponse.resume(response);
                } else {
                    vertx.setTimer(TASK_STATUS_CHECK_INTERVAL, tid -> run());
                }
            }
        };
        check.run();
    }

    private List<Task> listAll() {
        return Task.listAll();
    }

    private Task getTaskById(UUID uuid) {
        return getTaskByUuid(uuid)
            .orElseThrow(() -> new TaskNotFoundException(uuid));
    }

    private Optional<Task> getTaskByUuid(UUID id) {
        return Task.find("uuid", id).firstResultOptional();
    }

    private int getRandomProcessTime() {
        return new Random().nextInt(35, 80) * 100;
    }

}
