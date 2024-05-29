package ToDoIt.backend.repository;

import ToDoIt.backend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByUserID(String userID);

    Users findByEmailAndPassword(String email, String password);

    Users findByPhoneAndUserID(String phone, String userID);

    Users findByEmailAndPhoneAndUserID(String email, String phone, String userID);

}
