package ToDoIt.backend.service;

import ToDoIt.backend.repository.UserRepository;
import ToDoIt.backend.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveUser(Users user) {
        validateDuplicateUser(user);
        userRepository.save(user);
    }

    private void validateDuplicateUser(Users user) {
        Users existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail != null) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        Users existingUserByUserID = userRepository.findByUserID(user.getUserID());
        if (existingUserByUserID != null) {
            throw new IllegalStateException("이미 존재하는 사용자 ID입니다.");
        }
    }

    public Users findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Users login(String email, String password) {
        Users user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public Users findEmail(String phone, String userID) {
        return userRepository.findByPhoneAndUserID(phone, userID);
    }

    public Users findPass(String email, String phone, String userID) {
        return userRepository.findByEmailAndPhoneAndUserID(email, phone, userID);
    }

    @Transactional
    public void update(String email, String phone, String userID) {
        Users user = userRepository.findByEmail(email);
        user.setPhone(phone);
        user.setUserID(userID);
    }

    @Transactional
    public void deleteUser(String email) {
        Users user = userRepository.findByEmail(email);
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(String email, String newPassword) {
        Users user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
    }

}
