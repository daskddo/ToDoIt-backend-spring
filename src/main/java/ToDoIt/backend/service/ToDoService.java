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
                toDo.getId(),
                toDo.getUuid(),
                toDo.getTask(),
                toDo.getDueDate()
        )).collect(Collectors.toList());
    }

    @Transactional
    public List<ToDoDTO> getTodayToDosForUser(String email, LocalDate date) {
        List<ToDo> toDos = toDoRepository.findByUserEmailAndDueDate(email, date);

        // ToDoDTO로 변환
        return toDos.stream().map(toDo -> new ToDoDTO(
                toDo.getId(),
                toDo.getUuid(),
                toDo.getTask(),
                toDo.getDueDate()
        )).collect(Collectors.toList());
    }

    @Transactional
    public List<ToDoDTO> getAnytimeTasks(String email) {
        List<ToDo> toDos = toDoRepository.findByUserEmailAndDueDateIsNull(email);

        // ToDoDTO로 변환
        return toDos.stream().map(toDo -> new ToDoDTO(
                toDo.getId(),
                toDo.getUuid(),
                toDo.getTask(),
                toDo.getDueDate()
        )).collect(Collectors.toList());
    }

    @Transactional
    public void createToDoForUser(String email, ToDoDTO toDoDTO) {
        Users user = userRepository.findByEmail(email);

        ToDo toDo = new ToDo();
        toDo.setUser(user);
        toDo.setUuid(toDoDTO.getUuid());
        toDo.setTask(toDoDTO.getTask());
        toDo.setDueDate(toDoDTO.getDueDate());

        toDoRepository.save(toDo);
    }

    @Transactional
    public void updateToDoForUser(Long id, ToDoDTO toDoDTO) {
        ToDo existingToDo = toDoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ToDo not found"));

        existingToDo.setTask(toDoDTO.getTask());
        existingToDo.setUuid(toDoDTO.getUuid());
        existingToDo.setDueDate(toDoDTO.getDueDate());
        toDoRepository.save(existingToDo);
    }

    @Transactional
    public void deleteToDoForUser(Long id) {
        toDoRepository.deleteById(id);
    }
}