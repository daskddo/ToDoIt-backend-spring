package ToDoIt.backend.DTO;

import ToDoIt.backend.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userID;
    private String password;
    private String email;
    private String phone;
    private Role role;
}
