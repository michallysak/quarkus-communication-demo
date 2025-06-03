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
import pl.michallysak.utils.TestDataFactory;
import pl.michallysak.utils.TestGraphQLFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@Testcontainers
class TaskGraphQLResourceIT extends BaseIT {

    @Test
    void testCreateTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();

        // when
        Response response = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        response.body().prettyPrint(); // todo remove

        // then
        response.then().statusCode(200)
            .body("data.createTask.id", notNullValue())
            .body("data.createTask.name", equalTo(taskCreateDto.getName()));
    }

    @ParameterizedTest
    @MethodSource("invalidTaskNames")
    void testCreateTaskWithInvalidNameSize(String name) {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        taskCreateDto.setName(name);

        // when
        Response response = TestGraphQLFactory.Tasks.createTask(taskCreateDto);

        // then
        response.then().statusCode(200)
            .body("errors", notNullValue());
    }

    @Test
    void testGetTaskById() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        // and
        Response createResponse = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");

        // when
        Response response = TestGraphQLFactory.Tasks.getTaskById(id);
        response.body().prettyPrint(); // todo remove

        // then
        response.then().statusCode(200)
            .body("data.taskById.id", notNullValue())
            .body("data.taskById.name", equalTo(taskCreateDto.getName()));
    }

    @Test
    void testGetAllTasks() {
        // given
        TaskCreateDto taskCreateDto1 = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse1 = TestGraphQLFactory.Tasks.createTask(taskCreateDto1);
        createResponse1.then().statusCode(200);
        // and
        TaskCreateDto taskCreateDto2 = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse2 = TestGraphQLFactory.Tasks.createTask(taskCreateDto2);
        createResponse2.then().statusCode(200);

        // when
        Response response = TestGraphQLFactory.Tasks.getAll();
        response.body().prettyPrint(); // todo remove

        // then
        response.then().statusCode(200)
            .body("data.allTasks.size()", equalTo(2))
            .body("data.allTasks[0].id", notNullValue())
            .body("data.allTasks[0].name", equalTo(taskCreateDto1.getName()))
            .body("data.allTasks[1].id", notNullValue())
            .body("data.allTasks[1].name", equalTo(taskCreateDto2.getName()));
    }

    @Test
    void testUpdateTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");

        // when
        TaskDto updateDto = TestDataFactory.Tasks.createTaskDto(UUID.fromString(id));
        Response response = TestGraphQLFactory.Tasks.updateTask(id, updateDto);
        response.body().prettyPrint(); // todo remove

        // then
        response.then().statusCode(200)
            .body("data.updateTask.id", equalTo(id))
            .body("data.updateTask.name", equalTo(updateDto.getName()));
    }

    @ParameterizedTest
    @MethodSource("invalidTaskNames")
    void testUpdateTaskWithInvalidNameSize(String name) {
        // given
        TaskCreateDto validDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestGraphQLFactory.Tasks.createTask(validDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");
        // and
        TaskDto taskDto = TestDataFactory.Tasks.createTaskDto(UUID.fromString(id));
        taskDto.setName(name);

        // when
        Response response = TestGraphQLFactory.Tasks.updateTask(id, taskDto);
        response.body().prettyPrint(); // todo remove

        // then
        response.then().statusCode(200)
            .body("errors", notNullValue());
    }

    @Test
    void testDeleteTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");

        // when
        Response response = TestGraphQLFactory.Tasks.deleteTask(id);

        // then
        response.then().statusCode(200)
            .body("data.deleteTask", equalTo(true));
        // and
        TestGraphQLFactory.Tasks.getTaskById(id).then().statusCode(200).body("data.taskById", nullValue());
    }

    @Test
    void testDeleteAllTasks() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        TestGraphQLFactory.Tasks.createTask(taskCreateDto).then().statusCode(200);
        TestGraphQLFactory.Tasks.createTask(taskCreateDto).then().statusCode(200);

        // when
        Response getAllResponse = TestGraphQLFactory.Tasks.getAll();
        getAllResponse.then().statusCode(200);
        var ids = getAllResponse.jsonPath().getList("data.allTasks.id");
        for (Object id : ids) {
            TestGraphQLFactory.Tasks.deleteTask(id.toString())
                .then().statusCode(200)
                .body("data.deleteTask", equalTo(true));
        }
        // then
        TestGraphQLFactory.Tasks.getAll()
            .then().statusCode(200)
            .body("data.allTasks.size()", equalTo(0));
    }

    @Test
    void testProcessTask() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");

        // when
        Response response = TestGraphQLFactory.Tasks.processTask(id);

        // then
        response.then().statusCode(200)
            .body("data.processTask", equalTo(true));
    }

    @Test
    void testProcessTaskAlreadyProcess() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");
        // and
        TestGraphQLFactory.Tasks.processTask(id).then().statusCode(200)
            .body("data.processTask", equalTo(true));

        // when
        Response response = TestGraphQLFactory.Tasks.processTask(id);

        // then
        response.then().statusCode(200)
            .body("data.processTask", equalTo(false));
    }

    @Test
    void testTaskUpdatesSubscription() {
        // given
        TaskCreateDto taskCreateDto = TestDataFactory.Tasks.createTaskCreateDto();
        Response createResponse = TestGraphQLFactory.Tasks.createTask(taskCreateDto);
        createResponse.then().statusCode(200);
        String id = createResponse.jsonPath().getString("data.createTask.id");

        // Prepare GraphQL subscription message
        Response response = TestGraphQLFactory.Tasks.taskUpdates(id);

        // when: trigger an update
        TestGraphQLFactory.Tasks.processTask(id).then().statusCode(200);

        // then: expect an update message
        // TODO graphql use websocket to handle subscription,
        //  so after implementing websocket change test to use websocket client
        response.then().body("data.upstreamPublisher", anEmptyMap());
    }

    public static Stream<Arguments> invalidTaskNames() {
        return Stream.of(
            Arguments.of("a" .repeat(2)),
            Arguments.of("a" .repeat(33))
        );
    }


}
