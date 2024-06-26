package ToDoIt.backend.repository;

import ToDoIt.backend.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByUserEmailAndDueDate(String email, LocalDate dueDate);
    List<ToDo> findAllByUserEmail(String email);
}