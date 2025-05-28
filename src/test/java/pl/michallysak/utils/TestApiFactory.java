package pl.michallysak.utils;

import io.restassured.response.Response;
import pl.michallysak.task.domain.TaskCreateDto;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TestApiFactory {

    public static class Tasks {

        private final static String TASK_ENPOINT = "/task";

        public static Response createTask(TaskCreateDto taskCreateDto) {
            return given()
                .contentType("application/json")
                .body(taskCreateDto)
                .when().post(TASK_ENPOINT);
        }

        public static Response getTaskById(UUID taskId) {
            return given().when()
                .get(TASK_ENPOINT + "/{id}", taskId.toString());
        }

        public static Response getAll() {
            return given().when()
                .get(TASK_ENPOINT);
        }

        public static Response updateTask(UUID taskId, pl.michallysak.task.domain.TaskDto updateDto) {
            return given()
                .contentType("application/json")
                .body(updateDto)
                .when().put(TASK_ENPOINT + "/{id}", taskId);
        }

        public static Response deleteTask(UUID taskId) {
            return given().when()
                .delete(TASK_ENPOINT + "/{id}", taskId);
        }

        public static Response deleteAll() {
            return given().when()
                .delete(TASK_ENPOINT);
        }

    }


}
