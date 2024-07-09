package ToDoIt.backend.controller;

import ToDoIt.backend.DTO.ApiResponse;
import ToDoIt.backend.DTO.KakaoUserInfoResponseDto;
import ToDoIt.backend.domain.Users;
import ToDoIt.backend.jwt.JwtTokenUtil;
import ToDoIt.backend.service.KakaoService;
import ToDoIt.backend.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    // 카카오 서버에서 code를 받아 access token을 받아오기(프론트되면 추후 삭제)
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        return ResponseEntity.ok(accessToken);
    }

    // access token을 json으로 받을 때
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String accessToken = request.get("accessToken");
            KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

            log.info("User Email: {}", userInfo.getKakaoAccount().getEmail());
            log.info("User Nickname: {}", userInfo.getKakaoAccount().getProfile().getNickName());

            Users user = userService.findOrCreateUser(userInfo);
            String jwtToken = jwtTokenUtil.generateAccessToken(user);

            log.info("{\"result\": 1, \"resultCode\": 200, \"token\": \"{}\"}", jwtToken);
            return ResponseEntity.ok(new ApiResponse2(1,200,jwtToken));
        } catch (Exception e) {
            log.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0,600));
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiResponse2 {
        private int result;
        private int resultCode;
        private String token;

        public ApiResponse2(int result, int resultCode, String token) {
            this.result = result;
            this.resultCode = resultCode;
            this.token = token;
        }
    }
}