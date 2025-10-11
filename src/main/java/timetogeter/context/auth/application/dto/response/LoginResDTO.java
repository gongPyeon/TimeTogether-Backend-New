package timetogeter.context.auth.application.dto.response;

import timetogeter.context.auth.application.dto.TokenCommand;

public record LoginResDTO (TokenCommand token,
                           String imgIv,
                           String emailIv,
                           String phoneIv)
{
}
