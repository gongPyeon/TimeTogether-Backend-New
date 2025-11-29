package timetogeter.context.schedule.application.dto.response;

import java.util.List;

public record PromiseDetailResDTO(String scheduleId,
                                  String title,
                                  String type,
                                  String placeName,
                                  String groupName,
                                  List<String> encUserIds)
{}
