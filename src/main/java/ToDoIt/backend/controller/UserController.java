package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ChangePasswordDTO;
import ToDoIt.backend.DTO.UserDTO;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO request) {
        logger.info("Received login request with email: {} and password: {}", request.getEmail(), request.getPassword());
        Users user = userService.login(request.getEmail(), request.getPassword());

        if (user != null) {
            String token = jwtTokenUtil.generateAccessToken(user);
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200, \"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO request) {
        try {
            Users existingUser = userService.findUserByEmail(request.getEmail());

            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"resultCode\": 601}");
            }

            String encodedPassword = passwordEncoder.encode(request.getPassword());

            Users newUser = new Users();
            newUser.setUserID(request.getUserID());
            newUser.setPassword(encodedPassword);
            newUser.setEmail(request.getEmail());
            newUser.setPhone(request.getPhone());
            newUser.setRole(request.getRole());

            userService.saveUser(newUser);

            return ResponseEntity.ok("{\"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }

    @PostMapping("/emailFind")
    public ResponseEntity<String> emailFind(@RequestBody UserDTO request) {
        try {
            Users user = userService.findEmail(request.getPhone(), request.getUserID());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            return ResponseEntity.ok("{\"email\": \"" + user.getEmail() + "\", \"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during email find", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }

    @PostMapping("/passFind")
    public ResponseEntity<String> passFind(@RequestBody UserDTO request) {
        try {
            Users user = userService.findPass(request.getEmail(), request.getPhone(), request.getUserID());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during password find", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PutMapping("/passFind")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePasswordDTO request) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        token = token.substring(7);

        String userEmail = jwtTokenUtil.extractUsername(token);
        Users user = userService.findUserByEmail(userEmail);
        if (user != null) {
            userService.changePassword(userEmail, request.getPassword());
            return ResponseEntity.ok("{\"resultCode\": 200, \"message\": \"Password changed successfully\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"resultCode\": 600, \"message\": \"User not found\"}");
        }
    }

    @PutMapping("/infoChange/{email}")
    public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody @Valid UserDTO request) {

        try {
            Users user = userService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            userService.update(email, request.getPhone(), request.getUserID());

            return ResponseEntity.ok("{\"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during user update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
        try {
            Users user = userService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            userService.deleteUser(email);

            return ResponseEntity.ok("{\"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during user deletion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }
}