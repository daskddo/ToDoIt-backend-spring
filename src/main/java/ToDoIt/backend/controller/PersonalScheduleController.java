package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ApiResponse;
import ToDoIt.backend.DTO.PersonalScheduleDTO;
import ToDoIt.backend.domain.Users;
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
@RequestMapping("/users/{email}/schedules")
public class PersonalScheduleController {
    private final PersonalScheduleService personalScheduleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getPersonalSchedules(@PathVariable("email") String email) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            List<PersonalScheduleDTO> personalSchedulesList = personalScheduleService.getPersonalSchedulesByEmail(email);
            log.info("{\"result\": 1, \"resultCode\": 200, \"data\": {}}", personalSchedulesList);
            return ResponseEntity.ok(new ApiResponse2(1, 200, personalSchedulesList));
        } catch (Exception e) {
            log.error("Error during fetching schedules", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPersonalSchedule(@PathVariable("email") String email, @RequestBody @Valid PersonalScheduleDTO scheduleDTO) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            personalScheduleService.createPersonalSchedule(email, scheduleDTO);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during schedule creation", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @PutMapping("/{pscheduleID}")
    public ResponseEntity<?> updatePersonalSchedule(@PathVariable("email") String email, @PathVariable("pscheduleID") Long scheduleId, @RequestBody @Valid PersonalScheduleDTO scheduleDTO) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            personalScheduleService.updatePersonalSchedule(scheduleId, scheduleDTO);
            log.info("{\"result\": 1, \"resultCode\": 200}");
            return ResponseEntity.ok(new ApiResponse(1,200));
        } catch (Exception e) {
            log.error("Error during schedule update", e);
            log.info("{\"result\": 0, \"resultCode\": 600}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }
    }

    @DeleteMapping("/{pscheduleID}")
    public ResponseEntity<?> deletePersonalSchedule(@PathVariable("email") String email, @PathVariable("pscheduleID") Long scheduleId) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                log.info("{\"result\": 0, \"resultCode\": 404}");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(0,404));
            }

            personalScheduleService.deletePersonalSchedule(scheduleId);
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
        private List<PersonalScheduleDTO> data;

        public ApiResponse2(int result, int resultCode, List<PersonalScheduleDTO> data) {
            this.result = result;
            this.resultCode = resultCode;
            this.data = data;
        }
    }
}