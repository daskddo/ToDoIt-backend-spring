package ToDoIt.backend.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
    private int result;
    private int resultCode;

    public ApiResponse(int result, int resultCode) {
        this.result = result;
        this.resultCode = resultCode;
    }
}
