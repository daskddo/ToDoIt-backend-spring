package ToDoIt.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 프론트없이 테스트해보기 위한 코드(추후 삭제)
@Controller
@RequestMapping("/login")
public class KakaoLoginPageController {
    @Value("${kakao.client-id}")
    private String client_id;

    @Value("${kakao.redirect-uri}")
    private String redirect_uri;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        return "login";
    }
}