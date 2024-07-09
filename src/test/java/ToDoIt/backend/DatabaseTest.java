package ToDoIt.backend;

import ToDoIt.backend.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static ToDoIt.backend.domain.Role.USER;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DatabaseTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void 개인일정테스트() {
        // 유저 생성
        Users user = new Users();
        user.setNickname("test_user");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("123456789");
        user.setRole(USER);
        entityManager.persist(user);

        // 개인 일정 생성
        PersonalSchedules personalSchedule = new PersonalSchedules();
        personalSchedule.setTitle("Test Schedule");
        personalSchedule.setAllday("false");
        personalSchedule.setStartTime(String.valueOf(LocalDateTime.now()));
        personalSchedule.setEndTime(String.valueOf(LocalDateTime.now().plusHours(1)));
        personalSchedule.setUuid("uuid12");
        personalSchedule.setUser(user);
        personalSchedule.setColor("#AAAAAAA");
        entityManager.persist(personalSchedule);
        entityManager.flush();
        entityManager.clear();

        // 유저 조회
        Users retrievedUser = entityManager.find(Users.class, user.getUserNUM());
        assertNotNull(retrievedUser);
        assertEquals("test_user", retrievedUser.getNickname());

        // 개인 일정 조회
        PersonalSchedules retrievedSchedule = entityManager.find(PersonalSchedules.class, personalSchedule.getPScheduleID());
        assertNotNull(retrievedSchedule);
        assertEquals("Test Schedule", retrievedSchedule.getTitle());
    }
}