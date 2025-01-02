package com.pro.oauth2;

import com.pro.oauth2.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class testController {
    @GetMapping("/login-test")
    @ResponseBody
    public String loginInfo() {
        return "login";
    }

    @GetMapping("/logout-test")
    @ResponseBody
    public String logoutInfo() {
        return "logout"; // login page로 변경
    }
}
