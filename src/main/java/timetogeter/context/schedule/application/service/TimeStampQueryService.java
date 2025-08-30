package timetogeter.context.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogeter.context.schedule.application.dto.request.TimestampReqDTO;
import timetogeter.context.schedule.application.dto.response.TimestampResDTO;
import timetogeter.context.schedule.domain.repository.TimeStampRepository;

@Service
@RequiredArgsConstructor
public class TimeStampQueryService {
    private final TimeStampRepository timeStampRepository;

//    public TimestampResDTO getTimeStampList(TimestampReqDTO reqDTO) {
//        timeStampRepository.findTimeStampsByUserIdAndTimeStamp(reqDTO.dates());
//    }
}
