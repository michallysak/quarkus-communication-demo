package pl.michallysak.utils;

import io.restassured.response.Response;
import pl.michallysak.task.domain.TaskCreateDto;
import pl.michallysak.task.domain.TaskDto;

import static io.restassured.RestAssured.given;

public class TestGraphQLFactory {

    private static final String GRAPHQL_ENDPOINT = "/graphql";

    private static Response postQuery(String query) {
        String escapedQuery = query.replace("\\", "\\\\").replace("\"", "\\\"");
        String body = String.format("{\"query\":\"%s\"}", escapedQuery);
        System.out.println("GraphQL Request: " + GRAPHQL_ENDPOINT + " with (\"application/json\") body:\n" + body);
        return given().contentType("application/json").body(body).post(GRAPHQL_ENDPOINT);
    }


    public static class Tasks {

        public static Response createTask(TaskCreateDto taskCreateDto) {
            String mutation = String.format("mutation { createTask(taskCreateDto: { name: \"%s\" }) { id name } }", taskCreateDto.getName());
            return postQuery(mutation);
        }

        public static Response getTaskById(String id) {
            String query = String.format("query { taskById(id: \"%s\") { id name } }", id);
            return postQuery(query);
        }

        public static Response getAll() {
            String query = "query { allTasks { id name } }";
            return postQuery(query);
        }

        public static Response updateTask(String id, TaskDto updateDto) {
            String mutation = String.format("mutation { updateTask(id: \"%s\", taskDto: { id: \"%s\", name: \"%s\" }) { id name } }", id, id, updateDto.getName());
            return postQuery(mutation);
        }

        public static Response deleteTask(String id) {
            String mutation = String.format("mutation { deleteTask(id: \"%s\") }", id);
            return postQuery(mutation);
        }

        public static Response processTask(String id) {
            String mutation = String.format("mutation { processTask(id: \"%s\") }", id);
            return postQuery(mutation);
        }

        public static Response taskUpdates(String id) {
            String subscription = String.format("subscription { taskUpdates(id: \"%s\") { id name status } }", id);
            return postQuery(subscription);
        }
    }

}
