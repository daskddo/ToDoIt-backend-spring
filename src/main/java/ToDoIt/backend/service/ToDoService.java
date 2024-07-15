package ToDoIt.backend.service;

import ToDoIt.backend.DTO.ToDoDTO;
import ToDoIt.backend.domain.ToDo;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.repository.ToDoRepository;
import ToDoIt.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<ToDoDTO> getAllToDosForUser(String email) {
        List<ToDo> toDos = toDoRepository.findAllByUserEmail(email);

        // ToDoDTO로 변환
        return toDos.stream().map(toDo -> new ToDoDTO(
                toDo.getUuid(),
                toDo.getTaskTitle(),
                toDo.getTaskDate(),
                toDo.getIsComplete(),
                toDo.getColor()
        )).collect(Collectors.toList());
    }

    @Transactional
    public List<ToDoDTO> getTodayToDosForUser(String email, String date) {
        List<ToDo> toDos = toDoRepository.findAllByUserEmail(email);

        // ToDoDTO로 변환
        return toDos.stream()
                .filter(toDo -> toDo.getTaskDate() != null && toDo.getTaskDate().startsWith(date))
                .map(toDo -> new ToDoDTO(
                    toDo.getUuid(),
                    toDo.getTaskTitle(),
                    toDo.getTaskDate(),
                    toDo.getIsComplete(),
                    toDo.getColor()
        )).collect(Collectors.toList());
    }

    @Transactional
    public List<ToDoDTO> getAnytimeTasks(String email) {
        List<ToDo> toDos = toDoRepository.findByUserEmailAndTaskDateIsNull(email);

        // ToDoDTO로 변환
        return toDos.stream().map(toDo -> new ToDoDTO(
                toDo.getUuid(),
                toDo.getTaskTitle(),
                toDo.getTaskDate(),
                toDo.getIsComplete(),
                toDo.getColor()
        )).collect(Collectors.toList());
    }

    @Transactional
    public void createToDoForUser(String email, ToDoDTO toDoDTO) {
        Users user = userRepository.findByEmail(email);

        // taskDate에서 date부분 추출
        String taskDate = toDoDTO.getTaskDate();
        String formattedDate = null;
        if (taskDate != null && taskDate.length() >= 10) {
            formattedDate = taskDate.substring(0, 10);
        }

        ToDo toDo = new ToDo();
        toDo.setUser(user);
        toDo.setUuid(toDoDTO.getUuid());
        toDo.setTaskTitle(toDoDTO.getTaskTitle());
        toDo.setTaskDate(formattedDate);
        toDo.setIsComplete(toDoDTO.getIsComplete());
        toDo.setColor(toDoDTO.getColor());

        toDoRepository.save(toDo);
    }

    @Transactional
    public void updateToDoForUser(ToDoDTO toDoDTO, String email) {
        ToDo existingToDo = toDoRepository.findByUuidAndUserEmail(toDoDTO.getUuid(), email).orElseThrow(() -> new IllegalArgumentException("ToDo not found"));

        // taskDate에서 date부분 추출
        String taskDate = toDoDTO.getTaskDate();
        String formattedDate = null;
        if (taskDate != null && taskDate.length() >= 10) {
            formattedDate = taskDate.substring(0, 10);
        }

        existingToDo.setTaskTitle(toDoDTO.getTaskTitle());
        existingToDo.setTaskDate(formattedDate);
        existingToDo.setUuid(toDoDTO.getUuid());
        existingToDo.setIsComplete(toDoDTO.getIsComplete());
        existingToDo.setColor(toDoDTO.getColor());
        toDoRepository.save(existingToDo);
    }

    @Transactional
    public void deleteToDoForUser(String uuid, String email) {
        toDoRepository.deleteByUuidAndUserEmail(uuid, email);
    }
}