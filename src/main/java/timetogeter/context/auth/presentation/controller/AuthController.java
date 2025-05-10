package timetogeter.context.auth.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @GetMapping("/restDocsTest")
    public String restDocsTestAPI() {
        return "test!!";
    }

    @GetMapping("/restDocsTest/{id}")
    public String restDocsTestParameterAPI(@PathVariable Long id) {
        return id + "test!!";
    }
}
