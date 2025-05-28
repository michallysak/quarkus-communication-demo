package pl.michallysak.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.michallysak.task.domain.Task;

public class BaseIT {

    @Container
    private final static MongoDBContainer mongo = TestMongoContainer.create();

    @BeforeAll
    static void setup() {
        mongo.start();
    }

    @AfterAll
    static void tearDown() {
        mongo.stop();
    }

    @BeforeEach
    void setUp() {
        Task.deleteAll();
    }

}
