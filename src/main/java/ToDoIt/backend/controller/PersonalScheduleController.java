package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.PersonalScheduleDTO;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.service.PersonalScheduleService;
import ToDoIt.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{email}/schedules")
public class PersonalScheduleController {
    private final PersonalScheduleService personalScheduleService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PersonalScheduleController.class);

    @Autowired
    public PersonalScheduleController(PersonalScheduleService personalScheduleService, UserService userService) {
        this.personalScheduleService = personalScheduleService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getPersonalSchedules(@PathVariable("email") String email) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            return ResponseEntity.ok(personalScheduleService.getPersonalSchedulesByEmail(email));
        } catch (Exception e) {
            logger.error("Error during fetching schedules", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }

    @PostMapping
    public ResponseEntity<String> createPersonalSchedule(@PathVariable("email") String email, @RequestBody @Valid PersonalScheduleDTO scheduleDTO) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            personalScheduleService.createPersonalSchedule(email, scheduleDTO);
            return ResponseEntity.ok("{\"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during schedule creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }

    @PutMapping("/{pscheduleID}")
    public ResponseEntity<String> updatePersonalSchedule(@PathVariable("email") String email, @PathVariable("pscheduleID") Long scheduleId, @RequestBody @Valid PersonalScheduleDTO scheduleDTO) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            personalScheduleService.updatePersonalSchedule(scheduleId, scheduleDTO);
            return ResponseEntity.ok("{\"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during schedule update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }

    @DeleteMapping("/{pscheduleID}")
    public ResponseEntity<String> deletePersonalSchedule(@PathVariable("email") String email, @PathVariable("pscheduleID") Long scheduleId) {
        try {
            Users user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"resultCode\": 404}");
            }

            personalScheduleService.deletePersonalSchedule(scheduleId);
            return ResponseEntity.ok("{\"resultCode\": 200}");
        } catch (Exception e) {
            logger.error("Error during schedule deletion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"resultCode\": 600}");
        }
    }
}