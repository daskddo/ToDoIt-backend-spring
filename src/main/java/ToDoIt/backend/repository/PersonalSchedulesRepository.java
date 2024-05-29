package ToDoIt.backend.repository;

import ToDoIt.backend.domain.PersonalSchedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalSchedulesRepository extends JpaRepository<PersonalSchedules, Long> {
    List<PersonalSchedules> findByUserEmail(String email);
}