package timetogeter.context.auth.application.dto;

public record ApiCommand (String clientId,
                          String clientSecret,
                          String code,
                          String redirectUri){
}
