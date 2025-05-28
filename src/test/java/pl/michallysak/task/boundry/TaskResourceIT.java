package pl.michallysak.task.boundry;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.TaskDto;
import pl.michallysak.utils.BaseIT;
import pl.michallysak.utils.TestApiFactory;
import pl.michallysak.utils.TestDataFactory;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@Testcontainers
class TaskResourceIT extends BaseIT {

    @Test
    void testCreateTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();

        // when
        Response response = TestApiFactory.Tasks.createTask(taskCreateDto);

        // then
        response.then().statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo(taskCreateDto.getName()));
    }

    @ParameterizedTest
    @MethodSource("invalidTaskNames")
    void testCreateTaskWithInvalidNameSize(String name) {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        taskCreateDto.setName(name);

        // when
        Response response = TestApiFactory.Tasks.createTask(taskCreateDto);

        // then
        response.then().statusCode(400);
    }

    @Test
    void testGetTaskById() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        // and
        Response createResponse = TestApiFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(201);
        TaskDto taskDto = createResponse.as(TaskDto.class);
        UUID taskId = taskDto.getId();

        // when
        Response response = TestApiFactory.Tasks.getTaskById(taskId);

        // then
        response.then().statusCode(200)
            .body("id", notNullValue())
            .body("name", equalTo(taskCreateDto.getName()));
    }

    @Test
    void testGetAllTasks() {
        // given
        TaskCreateDto taskCreateDto1 = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse1 = TestApiFactory.Tasks.createTask(taskCreateDto1);
        createResponse1.then().statusCode(201);
        // and
        TaskCreateDto taskCreateDto2 = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse2 = TestApiFactory.Tasks.createTask(taskCreateDto2);
        createResponse2.then().statusCode(201);

        // when
        Response response = TestApiFactory.Tasks.getAll();

        // then
        response.then().statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].id", notNullValue())
            .body("[0].name", equalTo(taskCreateDto1.getName()))
            .body("[1].id", notNullValue())
            .body("[1].name", equalTo(taskCreateDto2.getName()));
    }

    @Test
    void testUpdateTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestApiFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(201);
        TaskDto createdTask = createResponse.as(TaskDto.class);
        UUID taskId = createdTask.getId();

        // when
        TaskDto updateDto = TestDataFactory.Tasks.createTaskDto(taskId);
        Response response = TestApiFactory.Tasks.updateTask(taskId, updateDto);

        // then
        response.then().statusCode(200)
            .body("id", equalTo(taskId.toString()))
            .body("name", equalTo(updateDto.getName()));
    }

    @ParameterizedTest
    @MethodSource("invalidTaskNames")
    void testUpdateTaskWithInvalidNameSize(String name) {
        // given
        TaskCreateDto validDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestApiFactory.Tasks.createTask(validDto);
        createResponse.then().statusCode(201);
        TaskDto createdTask = createResponse.as(TaskDto.class);
        UUID taskId = createdTask.getId();
        // and
        TaskDto taskDto = TestDataFactory.Tasks.createTaskDto(taskId);
        taskDto.setName(name);

        // when
        Response response = TestApiFactory.Tasks.updateTask(taskId, taskDto);

        // then
        response.then().statusCode(400);
    }

    @Test
    void testDeleteTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestApiFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(201);
        TaskDto createdTask = createResponse.as(TaskDto.class);
        UUID taskId = createdTask.getId();

        // when
        Response response = TestApiFactory.Tasks.deleteTask(taskId);

        // then
        response.then().statusCode(204);
        // and
        TestApiFactory.Tasks.getTaskById(taskId).then().statusCode(404);
    }

    @Test
    void testDeleteAllTasks() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        TestApiFactory.Tasks.createTask(taskCreateDto).then().statusCode(201);
        TestApiFactory.Tasks.createTask(taskCreateDto).then().statusCode(201);

        // when
        Response response = TestApiFactory.Tasks.deleteAll();

        // then
        response.then().statusCode(204);
        // and
        TestApiFactory.Tasks.getAll().then().statusCode(200).body("size()", equalTo(0));
    }

    public static Stream<Arguments> invalidTaskNames() {
        return Stream.of(
            Arguments.of("a".repeat(2)), // less than min size 3
            Arguments.of("a".repeat(33)) // more than max size 32
        );
    }

}
