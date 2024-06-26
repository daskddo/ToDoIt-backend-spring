package ToDoIt.backend.controller;

import ToDoIt.backend.domain.Role;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public AdminController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            token = token.substring(7);
            String userEmail = jwtTokenUtil.extractUsername(token);
            Users user = userService.findUserByEmail(userEmail);

            if (user == null || user.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            List<Users> allUsers = userService.findAllUsers();
            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            logger.error("Error during fetching all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }
}
