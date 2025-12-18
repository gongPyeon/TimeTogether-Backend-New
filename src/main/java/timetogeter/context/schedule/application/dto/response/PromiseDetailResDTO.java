package timetogeter.context.schedule.application.dto.response;

import java.util.List;

public record PromiseDetailResDTO(String scheduleId,
                                  String title,
                                  String type,
                                  int placeId,
                                  String placeName,
                                  String placeAddress,
                                  String groupId,
                                  String groupName,
                                  List<String> encUserIds)
{}
