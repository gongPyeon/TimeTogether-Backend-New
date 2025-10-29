package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.schedule.application.dto.request.TimestampReqDTO;
import timetogeter.context.schedule.application.dto.response.TimestampResDTO;
import timetogeter.context.schedule.domain.repository.TimeStampRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeStampQueryService {
    private final TimeStampRepository timeStampRepository;

    public TimestampResDTO getTimeStampList(String userId, TimestampReqDTO reqDTO) {
        List<String> timeStamps = timeStampRepository.findTimeStampsByUserIdAndTimeStamp(userId, reqDTO.dates());
        return new TimestampResDTO(timeStamps);
    }
}
