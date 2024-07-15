package ToDoIt.backend.repository;

import ToDoIt.backend.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findAllByUserEmail(String email);
    List<ToDo> findByUserEmailAndTaskDateIsNull(String email);
    Optional<ToDo> findByUuidAndUserEmail(String uuid, String email);
    void deleteByUuidAndUserEmail(String uuid, String email);
}