package timetogeter.context.promise.application.dto.request.detail;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

public record Promiseview2Request(
        @Schema(description = "그룹 아이디", example = "d71ac3eb-fc61-4cff-92c7-478a0e092936")
        String groupId,

        @Schema(description = "조회할 약속 아이디 리스트", example = """
                [
                  "e3f971e9-0e41-48b2-bb2e-b7594b98e170"
                ]
                """)
        List<String> promiseIdList

) {
}
