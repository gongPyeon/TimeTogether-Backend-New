package timetogeter.context._Log;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;


@Entity
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private long processingTime;
    private LocalDateTime createdAt;

    public RequestLog() {}

    public RequestLog(String url, long processingTime) {
        this.url = url;
        this.processingTime = processingTime;
        this.createdAt = LocalDateTime.now();
    }
}

