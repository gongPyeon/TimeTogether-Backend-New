package timetogeter.context.schedule.application.dto;

import java.util.List;
public record PromiseDetailDTO(String scheduleId,
                                  String title,
                                  String type,
                                  String placeName,
                                  String groupName)
{}