package pl.michallysak.task.control;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.*;
import pl.michallysak.task.domain.TaskStatus;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@MongoEntity(collection = "tasks")
public class Task extends PanacheMongoEntity {
    private UUID uuid;
    private String name;
    private TaskStatus status;
}
