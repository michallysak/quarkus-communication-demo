package pl.michallysak.task.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Task DTO with a UUID and a name.")
public class TaskDto extends TaskCreateDto {
    private UUID id;
    private TaskStatus status;
}
