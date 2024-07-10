package ToDoIt.backend.service;

import ToDoIt.backend.DTO.ToDoDTO;
import ToDoIt.backend.domain.ToDo;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.repository.ToDoRepository;
import ToDoIt.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                toDo.getIsComplete()
        )).collect(Collectors.toList());
    }

    @Transactional
    public List<ToDoDTO> getTodayToDosForUser(String email, LocalDate date) {
        List<ToDo> toDos = toDoRepository.findByUserEmailAndTaskDate(email, date);

        // ToDoDTO로 변환
        return toDos.stream().map(toDo -> new ToDoDTO(
                toDo.getUuid(),
                toDo.getTaskTitle(),
                toDo.getTaskDate(),
                toDo.getIsComplete()
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
                toDo.getIsComplete()
        )).collect(Collectors.toList());
    }

    @Transactional
    public void createToDoForUser(String email, ToDoDTO toDoDTO) {
        Users user = userRepository.findByEmail(email);

        ToDo toDo = new ToDo();
        toDo.setUser(user);
        toDo.setUuid(toDoDTO.getUuid());
        toDo.setTaskTitle(toDoDTO.getTaskTitle());
        toDo.setTaskDate(toDoDTO.getTaskDate());
        toDo.setIsComplete(toDoDTO.getIsComplete());

        toDoRepository.save(toDo);
    }

    @Transactional
    public void updateToDoForUser(ToDoDTO toDoDTO, String email) {
        ToDo existingToDo = toDoRepository.findByUuidAndUserEmail(toDoDTO.getUuid(), email).orElseThrow(() -> new IllegalArgumentException("ToDo not found"));

        existingToDo.setTaskTitle(toDoDTO.getTaskTitle());
        existingToDo.setTaskDate(toDoDTO.getTaskDate());
        existingToDo.setUuid(toDoDTO.getUuid());
        existingToDo.setIsComplete(toDoDTO.getIsComplete());
        toDoRepository.save(existingToDo);
    }

    @Transactional
    public void deleteToDoForUser(String uuid, String email) {
        toDoRepository.deleteByUuidAndUserEmail(uuid, email);
    }
}