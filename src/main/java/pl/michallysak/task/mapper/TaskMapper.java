package pl.michallysak.task.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.Task;
import pl.michallysak.task.domain.TaskDto;

import java.util.UUID;

@ApplicationScoped
public class TaskMapper {

     public TaskDto toDto(Task task) {
         TaskDto taskDto = new TaskDto();
         taskDto.setId(task.getUuid());
         taskDto.setName(task.getName());
         return taskDto;
     }

     public Task fromDto(TaskDto taskDTO) {
         Task task = new Task();
         task.setUuid(taskDTO.getId());
         task.setName(taskDTO.getName());
         return task;
     }

     public Task fromCreateDto(TaskCreateDto createTaskDto) {
         Task task = new Task();
         task.setUuid(UUID.randomUUID());
         task.setName(createTaskDto.getName());
         return task;
     }

}
