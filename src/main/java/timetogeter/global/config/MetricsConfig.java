package timetogeter.global.config;

import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public ProcessorMetrics processorMetrics() {
        // 빈 등록하지 않도록 막음
        return null;
    }
}
