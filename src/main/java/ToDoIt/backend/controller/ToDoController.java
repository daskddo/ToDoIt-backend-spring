package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ToDoDTO;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.ToDoService;
import ToDoIt.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 404}");
            }

            return ResponseEntity.ok(toDoService.getAllToDosForUser(userEmail));
        }catch (Exception e) {
            log.error("Error during fetching all todos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayToDos(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 404}");
            }

            LocalDate today = LocalDate.now();
            return ResponseEntity.ok(toDoService.getTodayToDosForUser(userEmail,today));
        }catch (Exception e) {
            log.error("Error during fetching todos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @GetMapping("/anytime")
    public ResponseEntity<?> getAnytimeTasks(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 404}");
            }

            return ResponseEntity.ok(toDoService.getAnytimeTasks(userEmail));
        }catch (Exception e) {
            log.error("Error during fetching todos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PostMapping
    public ResponseEntity<?> createToDo(@RequestHeader("Authorization") String token, @RequestBody ToDoDTO toDoDTO) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 404}");
            }

            toDoService.createToDoForUser(userEmail,toDoDTO);
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        }catch (Exception e){
            log.error("Error during todo creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateToDo(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @RequestBody ToDoDTO toDoDTO) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 404}");
            }

            toDoService.updateToDoForUser(id, toDoDTO);
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        }catch (Exception e) {
            log.error("Error during todo update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteToDo(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            token = token.substring(7);

            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 404}");
            }

            toDoService.deleteToDoForUser(id);
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        }catch (Exception e) {
            log.error("Error during todo deletion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }
}