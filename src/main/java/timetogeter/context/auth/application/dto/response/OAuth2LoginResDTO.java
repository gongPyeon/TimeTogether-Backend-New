package timetogeter.context.auth.application.dto.response;

import timetogeter.context.auth.application.dto.TokenCommand;

public record OAuth2LoginResDTO (TokenCommand token,
                                 String wrappedDEK)
{
}
