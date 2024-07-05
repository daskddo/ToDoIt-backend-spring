package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    @Column(name = "Task", nullable = false)
    @NotEmpty(message = "Task is required")
    private String task;

    @Column(name = "DueDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    // String uuid
    @Column(name = "Uuid")
    private String uuid;
}