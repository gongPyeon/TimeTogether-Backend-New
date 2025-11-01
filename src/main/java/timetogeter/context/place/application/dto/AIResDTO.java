package timetogeter.context.place.application.dto;

import timetogeter.context.place.application.dto.response.PlaceRegisterResDTO;

import java.util.List;

public record AIResDTO(
    int code,
    String message,
    List<PlaceRegisterResDTO> result){
}
