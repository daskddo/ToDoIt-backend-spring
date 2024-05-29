package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PersonalSchedules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "P_ScheduleID")
    private Long pScheduleID;

    @ManyToOne
    @JoinColumn(name = "UserNUM")
    private Users user;

    @Column(name = "Title", nullable = false)
    @NotEmpty(message = "Title is required")
    private String title;

    @Column(name = "StartTime", nullable = false)
    @NotNull(message = "Start time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Column(name = "EndTime", nullable = false)
    @NotNull(message = "End time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Column(name = "Description")
    private String description;

    @Column(name = "Color", nullable = false)
    @NotEmpty(message = "Color is required")
    private String color;
}
