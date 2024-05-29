package ToDoIt.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalScheduleDTO {
    private Long pScheduleID;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String color;
}