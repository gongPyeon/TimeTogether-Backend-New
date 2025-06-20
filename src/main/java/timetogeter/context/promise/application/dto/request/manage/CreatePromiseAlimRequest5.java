package timetogeter.context.promise.application.dto.request.manage;

import java.util.HashMap;
import java.util.List;

public record CreatePromiseAlimRequest5(
        String promiseId,

        String encPromiseId,
        String encPromiseMemberId,
        String encUserId,
        String encPromiseKey,

        //단순
        List<String> encEncGroupKeyList, //< <encGroupKey(개인키로 암호화한 그룹키) 리스트>원소 각각으로 약속 공유키 암호화 한 리스트>
        List<HashMap<String,Integer>> whichEncUserIdsIn//<encUserId(그룹키로 암호화한 사용자 고유 아이디)의 약속 참여 여부 리스트> 를 담아서 요청

) {
}

