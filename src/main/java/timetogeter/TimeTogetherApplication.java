package timetogeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(exclude = {
        org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration.class
})
public class TimeTogetherApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimeTogetherApplication.class, args);
    }
}