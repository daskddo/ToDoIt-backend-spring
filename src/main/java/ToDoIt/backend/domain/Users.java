package ToDoIt.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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

    // nickname으로 변경
    @Column(name = "Nickname", unique = true, nullable = false)
    @NotEmpty(message = "Nickname is required")
    private String nickname;

    @Column(name = "Password", nullable = false)
    @NotEmpty(message = "Password is required")
    private String password;

    @Column(name = "Email", unique = true, nullable = false)
    @NotEmpty(message = "Email is required")
    @Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message="이메일 주소 양식을 확인해주세요")
    private String email;

    @Column(name = "Phone", nullable = false)
    @NotEmpty(message = "Phone is required")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private Role role;
}