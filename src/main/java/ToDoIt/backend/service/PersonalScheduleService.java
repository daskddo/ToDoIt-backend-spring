package ToDoIt.backend.service;

import ToDoIt.backend.repository.PersonalSchedulesRepository;
import ToDoIt.backend.repository.UserRepository;
import ToDoIt.backend.DTO.PersonalScheduleDTO;
import ToDoIt.backend.domain.PersonalSchedules;
import ToDoIt.backend.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalScheduleService {
    private final PersonalSchedulesRepository personalSchedulesRepository;
    private final UserRepository userRepository;

    public List<PersonalScheduleDTO> getPersonalSchedulesByEmail(String email) {
        List<PersonalSchedules> schedules = personalSchedulesRepository.findByUserEmail(email);

        // PersonalScheduleDTO로 변환
        return schedules.stream().map(schedule -> new PersonalScheduleDTO(
                schedule.getPScheduleID(),
                schedule.getTitle(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getDescription(),
                schedule.getColor()
        )).collect(Collectors.toList());
    }

    @Transactional
    public void createPersonalSchedule(String email, PersonalScheduleDTO scheduleDTO) {
        Users user = userRepository.findByEmail(email);

        PersonalSchedules schedule = new PersonalSchedules();
        schedule.setUser(user);
        schedule.setTitle(scheduleDTO.getTitle());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setDescription(scheduleDTO.getDescription());
        schedule.setColor(scheduleDTO.getColor());

        personalSchedulesRepository.save(schedule);
    }

    @Transactional
    public void updatePersonalSchedule(Long scheduleId, PersonalScheduleDTO scheduleDTO) {
        PersonalSchedules schedule = personalSchedulesRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        schedule.setTitle(scheduleDTO.getTitle());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setDescription(scheduleDTO.getDescription());
        schedule.setColor(scheduleDTO.getColor());

        personalSchedulesRepository.save(schedule);
    }

    @Transactional
    public void deletePersonalSchedule(Long scheduleId) {
        personalSchedulesRepository.deleteById(scheduleId);
    }
}