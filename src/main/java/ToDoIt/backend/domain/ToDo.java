package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ToDoID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UserNUM", nullable = false)
    private Users user;

    @Column(name = "TaskTitle", nullable = false)
    @NotEmpty(message = "TaskTitle is required")
    private String taskTitle;

    @Column(name = "TaskDate")
    private String taskDate;

    @Column(name = "Uuid")
    private String uuid;

    @Column(name = "IsComplete", nullable = false)
    @NotEmpty(message = "IsComplete is required")
    @Pattern(regexp = "true|false", message = "IsComplete must be 'true' or 'false'")
    private String isComplete;

    @Column(name = "Color", nullable = false)
    @NotEmpty(message = "Color is required")
    private String color;
}