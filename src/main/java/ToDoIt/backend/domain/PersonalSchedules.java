package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    // allday(true,false) 컬럼 추가
    @Column(name = "Allday", nullable = false)
    @NotEmpty(message = "Allday is required")
    @Pattern(regexp = "true|false", message = "Allday must be 'true' or 'false'")
    private String allday;

    // string으로 변경
    @Column(name = "StartTime", nullable = false)
    @NotEmpty(message = "Start time is required")
    private String  startTime;

    // string으로 변경
    @Column(name = "EndTime", nullable = false)
    @NotEmpty(message = "End time is required")
    private String endTime;

    @Column(name = "Description")
    private String description;

    @Column(name = "Color", nullable = false)
    @NotEmpty(message = "Color is required")
    private String color;

    // String uuid
    @Column(name = "Uuid")
    private String uuid;
}