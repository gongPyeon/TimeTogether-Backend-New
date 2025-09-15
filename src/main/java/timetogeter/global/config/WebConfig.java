package timetogeter.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import timetogeter.global.interceptor.measure.MeasuringInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MeasuringInterceptor measuringInterceptor;

    public WebConfig(MeasuringInterceptor measuringInterceptor) {
        this.measuringInterceptor = measuringInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(measuringInterceptor)
                .addPathPatterns("/**");
    }
}
