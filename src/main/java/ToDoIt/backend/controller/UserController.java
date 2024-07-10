package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ApiResponse;
import ToDoIt.backend.DTO.ChangePasswordDTO;
import ToDoIt.backend.DTO.UserDTO;
import ToDoIt.backend.domain.Role;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    public ResponseEntity<?> loginUser(@RequestBody UserDTO request) {
        log.info("Received login request with email: {} and password: {}", request.getEmail(), request.getPassword());
        Users user = userService.login(request.getEmail(), request.getPassword());

        if (user != null) {
            String token = jwtTokenUtil.generateAccessToken(user);
            log.info("{\"result\": 1, \"resultCode\": 200, \"token\": \"{}\"}", token);
            return ResponseEntity.ok(new ApiResponse3(1,200,token));
        } else {
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,600));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO request) {
        try {
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            Users newUser = new Users();
            newUser.setNickname(request.getNickname());
            newUser.setPassword(encodedPassword);
            newUser.setEmail(request.getEmail());
            newUser.setPhone(request.getPhone());
            newUser.setRole(Role.USER);

            userService.saveUser(newUser);

            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during registration", e);
            if (e.getMessage().contains("이메일 주소 양식을 확인해주세요")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(0,601));
            } else if (e.getMessage().contains("이미 존재하는 닉네임입니다.")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(0,602));
            } else if (e.getMessage().contains("이미 존재하는 이메일입니다.")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(0,603));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
            }
        }
    }

    @PostMapping("/emailFind")
    public ResponseEntity<?> emailFind(@RequestBody UserDTO request) {
        try {
            Users user = userService.findEmail(request.getPhone(), request.getNickname());

            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            log.info("{\"result\": 1, \"resultCode\": 200, \"email\": \"{}\"}", user.getEmail());
            return ResponseEntity.ok(new ApiResponse2(1,200,user.getEmail()));
        } catch (Exception e) {
            log.error("Error during email find", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PostMapping("/passFind")
    public ResponseEntity<?> passFind(@RequestBody UserDTO request) {
        try {
            Users user = userService.findPass(request.getEmail(), request.getPhone());

            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            log.info("{\"result\": 1, \"resultCode\": 200, \"email\": \"{}\"}", user.getEmail());
            return ResponseEntity.ok(new ApiResponse2(1,200,user.getEmail()));
        } catch (Exception e) {
            log.error("Error during password find", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,600));
        }
    }

    @PutMapping("/passReset")
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordDTO request) {
        try {
            Users user = userService.findUserByEmail(request.getEmail());

            if (user == null){
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            userService.changePassword(user.getEmail(), request.getPassword());
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        }catch (Exception e) {
            log.error("Error during password change", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(0,600));
        }
    }

    @PutMapping("/infoChange")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @RequestBody @Valid UserDTO request) {
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

            userService.update(userEmail, request.getPhone(), request.getNickname());
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during user update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
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

            userService.deleteUser(userEmail);
            log.info("{\"result\": 0, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during user deletion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiResponse2 {
        private int result;
        private int resultCode;
        private String email;

        public ApiResponse2(int result, int resultCode, String email) {
            this.result = result;
            this.resultCode = resultCode;
            this.email = email;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiResponse3 {
        private int result;
        private int resultCode;
        private String token;

        public ApiResponse3(int result, int resultCode, String token) {
            this.result = result;
            this.resultCode = resultCode;
            this.token = token;
        }
    }
}