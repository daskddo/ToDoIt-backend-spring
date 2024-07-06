package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ChangePasswordDTO;
import ToDoIt.backend.DTO.UserDTO;
import ToDoIt.backend.domain.Role;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO request) {
        log.info("Received login request with email: {} and password: {}", request.getEmail(), request.getPassword());
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"result\": 0, \"resultCode\": 601}");
            }

            String encodedPassword = passwordEncoder.encode(request.getPassword());

            Users newUser = new Users();
            newUser.setUserID(request.getUserID());
            newUser.setPassword(encodedPassword);
            newUser.setEmail(request.getEmail());
            newUser.setPhone(request.getPhone());
            newUser.setRole(Role.USER);

            userService.saveUser(newUser);
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        } catch (Exception e) {
            log.error("Error during registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PostMapping("/emailFind")
    public ResponseEntity<String> emailFind(@RequestBody UserDTO request) {
        try {
            Users user = userService.findEmail(request.getPhone(), request.getUserID());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": 0, \"resultCode\": 404}");
            }

            return ResponseEntity.ok("{\"email\": \"" + user.getEmail() + "\"}");
        } catch (Exception e) {
            log.error("Error during email find", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PostMapping("/passFind")
    public ResponseEntity<String> passFind(@RequestBody UserDTO request) {
        try {
            Users user = userService.findPass(request.getEmail(), request.getPhone(), request.getUserID());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": 0, \"resultCode\": 404}");
            }

            return ResponseEntity.ok("{\"email\": \"" + user.getEmail() + "\"}");
        } catch (Exception e) {
            log.error("Error during password find", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PutMapping("/passReset")
    public ResponseEntity<String> resetPassword(@RequestBody ChangePasswordDTO request) {
        try {
            Users user = userService.findUserByEmail(request.getEmail());

            if (user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": 0, \"resultCode\": 404}");
            }

            userService.changePassword(user.getEmail(), request.getPassword());
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        }catch (Exception e) {
            log.error("Error during password change", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @PutMapping("/infoChange/{email}")
    public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody @Valid UserDTO request) {
        try {
            Users user = userService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": 0, \"resultCode\": 404}");
            }

            userService.update(email, request.getPhone(), request.getUserID());
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        } catch (Exception e) {
            log.error("Error during user update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
        try {
            Users user = userService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"result\": 0, \"resultCode\": 404}");
            }

            userService.deleteUser(email);
            return ResponseEntity.ok("{\"result\": 1, \"resultCode\": 200}");
        } catch (Exception e) {
            log.error("Error during user deletion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\": 0, \"resultCode\": 600}");
        }
    }
}