package ToDoIt.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO {
    private String uuid;
    private String taskTitle;
    private LocalDate taskDate;
    private String isComplete;
}