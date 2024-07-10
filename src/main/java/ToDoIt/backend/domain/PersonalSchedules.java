package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "Allday", nullable = false)
    @NotEmpty(message = "Allday is required")
    @Pattern(regexp = "true|false", message = "Allday must be 'true' or 'false'")
    private String allday;

    @Column(name = "StartTime", nullable = false)
    @NotEmpty(message = "Start time is required")
    private String  startTime;

    @Column(name = "EndTime", nullable = false)
    @NotEmpty(message = "End time is required")
    private String endTime;

    @Column(name = "Description")
    private String description;

    @Column(name = "Color", nullable = false)
    @NotEmpty(message = "Color is required")
    private String color;

    @Column(name = "Uuid")
    private String uuid;
}