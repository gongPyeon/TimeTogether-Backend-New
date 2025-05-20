package timetogeter.global.security.util.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 배포 후 true로 변경
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    // cookie.setHttpOnly(true); // 원래 설정과 일치하도록
                    // cookie.setSecure(true);   // HTTPS 환경이라면
                    response.addCookie(cookie);
                }
            }
        }
    }
}
