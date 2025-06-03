package pl.michallysak.task.mapper;

import org.junit.jupiter.api.Test;
import pl.michallysak.task.control.Task;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.TaskDto;
import pl.michallysak.task.domain.TaskStatus;
import pl.michallysak.utils.TestDataFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskMapperTest {

    TaskMapper taskMapper = new TaskMapper();

    @Test
    void testMapToDto() {
        // given
        Task task = TestDataFactory.Tasks.createTask();

        // when
        TaskDto taskDto = taskMapper.toDto(task);

        // then
        assertNotNull(taskDto);
        assertEquals(task.getUuid(), taskDto.getId());
        assertEquals(task.getName(), taskDto.getName());
        assertEquals(task.getStatus(), taskDto.getStatus());
    }

    @Test
    void testMapToEntity() {
        // given
        TaskDto taskDto = TestDataFactory.Tasks.createTaskDto();

        // when
        Task task = taskMapper.fromDto(taskDto);

        // then
        assertNotNull(task);
        assertEquals(taskDto.getId(), task.getUuid());
        assertEquals(taskDto.getName(), task.getName());
        assertEquals(taskDto.getStatus(), task.getStatus());
    }

    @Test
    void testFromCreateDto() {
        // given
        TaskCreateDto createTaskDto = TestDataFactory.Tasks.createTaskCreateDto();

        // when
        Task task = taskMapper.fromCreateDto(createTaskDto);

        // then
        assertNotNull(task);
        assertNotNull(task.getUuid());
        assertEquals(createTaskDto.getName(), task.getName());
        assertEquals(TaskStatus.CREATED, task.getStatus());
    }
}
