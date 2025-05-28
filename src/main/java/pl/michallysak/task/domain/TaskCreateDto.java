package pl.michallysak.task.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Create Task DTO with  name.")
public class TaskCreateDto {

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 32)
    @Schema(examples = {"Task 1", "Task 2"})
    private String name;
}
