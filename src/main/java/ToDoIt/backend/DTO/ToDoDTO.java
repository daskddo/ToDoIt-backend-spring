package ToDoIt.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDTO {
    private String uuid;
    private String taskTitle;
    private String taskDate;
    private String isComplete;
    private String color;
}