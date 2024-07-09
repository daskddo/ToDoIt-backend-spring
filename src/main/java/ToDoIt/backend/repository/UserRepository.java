package ToDoIt.backend.repository;

import ToDoIt.backend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByNickname(String nickname);

    Users findByPhoneAndNickname(String phone, String nickname);

    Users findByEmailAndPhone(String email, String phone);
}