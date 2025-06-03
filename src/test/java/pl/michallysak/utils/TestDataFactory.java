package pl.michallysak.utils;

import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.control.Task;
import pl.michallysak.task.domain.TaskDto;

import java.util.UUID;

public class TestDataFactory {

    public static class Tasks {
        public final static String TASK_NAME = "Test ";

        public static TaskCreateDto createTaskCreateDto() {
            TaskCreateDto createTaskDto = new TaskCreateDto();
            createTaskDto.setName(TASK_NAME);
            return createTaskDto;
        }

        public static TaskDto createTaskDto() {
            TaskDto value = new TaskDto();
            value.setName(TASK_NAME);
            value.setId(UUID.randomUUID());
            return value;
        }

        public static TaskDto createTaskDto(UUID taskId) {
            TaskDto taskDto = new TaskDto();
            taskDto.setId(taskId);
            taskDto.setName("Updated Name");
            return taskDto;
        }
        public static Task createTask() {
            Task task = new Task();
            task.setName(TASK_NAME);
            return task;
        }

    }


}
