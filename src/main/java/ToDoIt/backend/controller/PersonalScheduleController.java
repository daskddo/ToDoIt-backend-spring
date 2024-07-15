package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ApiResponse;
import ToDoIt.backend.DTO.PersonalScheduleDTO;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.PersonalScheduleService;
import ToDoIt.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/schedules")
public class PersonalScheduleController {
    private final PersonalScheduleService personalScheduleService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<?> getPersonalSchedules(@RequestHeader("Authorization") String token) {
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

            List<PersonalScheduleDTO> personalSchedulesList = personalScheduleService.getPersonalSchedulesByEmail(userEmail);
            log.info("{\"result\": 1, \"resultCode\": 200, \"scheduleData\": {}}", personalSchedulesList);
            return ResponseEntity.ok(new ApiResponse2(1, 200, personalSchedulesList));
        } catch (Exception e) {
            log.error("Error during fetching schedules", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPersonalSchedule(@RequestHeader("Authorization") String token, @RequestBody @Valid PersonalScheduleDTO scheduleDTO) {
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

            personalScheduleService.createPersonalSchedule(userEmail, scheduleDTO);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during schedule creation", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PutMapping()
    public ResponseEntity<?> updatePersonalSchedule(@RequestHeader("Authorization") String token, @RequestBody @Valid PersonalScheduleDTO scheduleDTO) {
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

            personalScheduleService.updatePersonalSchedule(scheduleDTO, userEmail);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during schedule update", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deletePersonalSchedule(@RequestHeader("Authorization") String token, @PathVariable("uuid") String uuid) {
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

            personalScheduleService.deletePersonalSchedule(uuid, userEmail);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during schedule deletion", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    // ApiResponse 클래스는 JSON 응답을 위한 DTO 클래스입니다.
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiResponse2 {
        private int result;
        private int resultCode;
        private List<PersonalScheduleDTO> scheduleData;

        public ApiResponse2(int result, int resultCode, List<PersonalScheduleDTO> scheduleData) {
            this.result = result;
            this.resultCode = resultCode;
            this.scheduleData = scheduleData;
        }
    }
}