package pl.michallysak.task.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.Task;
import pl.michallysak.task.domain.TaskDto;
import pl.michallysak.task.exception.TaskNotFoundException;
import pl.michallysak.task.mapper.TaskMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskController {

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

}
