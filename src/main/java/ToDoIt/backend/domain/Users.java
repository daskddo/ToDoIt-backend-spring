package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserNUM")
    private Long userNUM;

    @Column(name = "UserID", unique = true, nullable = false)
    @NotEmpty(message = "UserID is required")
    private String userID;

    @Column(name = "Password", nullable = false)
    @NotEmpty(message = "Password is required")
    private String password;

    @Column(name = "Email", unique = true, nullable = false)
    @NotEmpty(message = "Email is required")
    private String email;

    @Column(name = "Phone", nullable = false)
    @NotEmpty(message = "Phone is required")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private Role role;
}
