package timetogeter.context.group.presentation.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import timetogeter.global.RestDocsSupport;

@WebMvcTest(GroupDetailController.class)
class GroupDetailControllerTest extends RestDocsSupport {

    @Nested
    @DisplayName("그룹 정보 수정 API (/api/v1/group/edit)")
    class editGroup {

        @Test
        @DisplayName("✅ 정상적으로 그룹 정보를 수정 할 수 있다.")
        @WithMockUser
        void editGroup_success() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 인증 토큰이 없으면 그룹 정보를 수정할 수 없다.")
        void editGroup_withoutToken_failure() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 그룹 내 방장이 아니면 그룹 정보를 수정할 수 없다.")
        @WithMockUser
        void editGroup_withNormalUserToken_failure() throws Exception {
            // given, when, then
        }
    }

    @Nested
    @DisplayName("그룹 초대 코드 생성 API (/api/v1/group/invite)")
    class inviteGroup {

        @Test
        @DisplayName("✅ 정상적으로 그룹 초대 코드를 생성 할 수 있다.")
        @WithMockUser
        void inviteGroup_success() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 인증 토큰이 없으면 그룹 초대 코드를 생성할 수 없다.")
        void inviteGroup_withoutToken_failure() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 그룹 내 그룹멤버가 아니면 그룹 초대 코드를 생성할 수 없다.")
        @WithMockUser
        void inviteGroup_withoutGroupUserToken_failure() throws Exception {
            // given, when, then
        }
    }


}