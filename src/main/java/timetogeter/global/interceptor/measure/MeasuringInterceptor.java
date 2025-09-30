package timetogeter.global.interceptor.measure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import timetogeter.context._Log.RequestLog;
import timetogeter.context._Log.RequestLogRepository;

@Component
public class MeasuringInterceptor implements HandlerInterceptor {

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        long currentTime = System.currentTimeMillis();
        request.setAttribute("bTime", currentTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        long currentTime = System.currentTimeMillis();
        long beginTime = (long) request.getAttribute("bTime");
        long processedTime = currentTime - beginTime;

        String url = request.getRequestURI();
        requestLogRepository.save(new RequestLog(url, processedTime));
    }
}
