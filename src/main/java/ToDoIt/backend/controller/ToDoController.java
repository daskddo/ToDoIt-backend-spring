package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ApiResponse;
import ToDoIt.backend.DTO.ToDoDTO;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.ToDoService;
import ToDoIt.backend.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class ToDoController {
    private final ToDoService toDoService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/all")
    public ResponseEntity<?> getAllToDos(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.info("{\"result\": 0, \"resultCode\": 403}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,403));
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,404));
            }

            List<ToDoDTO> allToDosForUser = toDoService.getAllToDosForUser(userEmail);
            log.info("{\"result\": 1, \"resultCode\": 200, \"data\": {}}", allToDosForUser);
            return ResponseEntity.ok(new ApiResponse2(1,200,allToDosForUser));
        } catch (Exception e) {
            log.error("Error during fetching all todos", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayToDos(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.info("{\"result\": 0, \"resultCode\": 403}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,403));
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,404));
            }

            LocalDate today = LocalDate.now();
            List<ToDoDTO> todayToDosForUser = toDoService.getTodayToDosForUser(userEmail, today);
            log.info("{\"result\": 1, \"resultCode\": 200, \"data\": {}}", todayToDosForUser);
            return ResponseEntity.ok(new ApiResponse2(1,200,todayToDosForUser));
        } catch (Exception e) {
            log.error("Error during fetching todos", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @GetMapping("/anytime")
    public ResponseEntity<?> getAnytimeTasks(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.info("{\"result\": 0, \"resultCode\": 403}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,403));
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,404));
            }

            List<ToDoDTO> anytimeTasks = toDoService.getAnytimeTasks(userEmail);
            log.info("{\"result\": 1, \"resultCode\": 200, \"data\": {}}", anytimeTasks);
            return ResponseEntity.ok(new ApiResponse2(1,200,anytimeTasks));
        } catch (Exception e) {
            log.error("Error during fetching todos", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PostMapping
    public ResponseEntity<?> createToDo(@RequestHeader("Authorization") String token, @RequestBody ToDoDTO toDoDTO) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.info("{\"result\": 0, \"resultCode\": 403}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,403));
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,404));
            }

            toDoService.createToDoForUser(userEmail, toDoDTO);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during todo creation", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateToDo(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @RequestBody ToDoDTO toDoDTO) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.info("{\"result\": 0, \"resultCode\": 403}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,403));
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,404));
            }

            toDoService.updateToDoForUser(id, toDoDTO);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during todo update", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteToDo(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.info("{\"result\": 0, \"resultCode\": 403}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,403));
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,404));
            }

            toDoService.deleteToDoForUser(id);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during todo deletion", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiResponse2 {
        private int result;
        private int resultCode;
        private List<ToDoDTO> data;

        public ApiResponse2(int result, int resultCode, List<ToDoDTO> data) {
            this.result = result;
            this.resultCode = resultCode;
            this.data = data;
        }
    }
}