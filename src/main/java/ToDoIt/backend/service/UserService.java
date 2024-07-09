package ToDoIt.backend.service;

import ToDoIt.backend.DTO.KakaoUserInfoResponseDto;
import ToDoIt.backend.domain.Role;
import ToDoIt.backend.repository.UserRepository;
import ToDoIt.backend.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        Users existingUserByUserID = userRepository.findByNickname(user.getNickname());
        if (existingUserByUserID != null) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    @Transactional
    public Users findOrCreateUser(KakaoUserInfoResponseDto userInfo) {
        Users existingUser = userRepository.findByEmail(userInfo.getKakaoAccount().getEmail());
        if (existingUser != null) {
            return existingUser;
        }

        Users newUser = new Users();
        newUser.setEmail(userInfo.getKakaoAccount().getEmail());
        newUser.setNickname(userInfo.getKakaoAccount().getProfile().getNickName());
        newUser.setPassword(passwordEncoder.encode("password")); // 기본 비밀번호
        newUser.setPhone("010-0000-0000"); // 기본 핸드폰 번호
        newUser.setRole(Role.USER); // 기본 역할 설정

        userRepository.save(newUser);
        return newUser;
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

    public Users findEmail(String phone, String nickname) {
        return userRepository.findByPhoneAndNickname(phone, nickname);
    }

    public Users findPass(String email, String phone) {
        return userRepository.findByEmailAndPhone(email, phone);
    }

    @Transactional
    public void update(String email, String phone, String nickname) {
        Users user = userRepository.findByEmail(email);
        user.setPhone(phone);
        user.setNickname(nickname);
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