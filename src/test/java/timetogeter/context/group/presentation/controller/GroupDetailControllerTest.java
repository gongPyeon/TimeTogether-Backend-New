/*
package timetogeter.context.group.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import timetogeter.context.auth.application.dto.RegisterResponse;
import timetogeter.context.auth.application.dto.RegisterUserCommand;
import timetogeter.context.auth.domain.adaptor.UserPrincipal;
import timetogeter.context.auth.domain.entity.User;
import timetogeter.context.auth.domain.vo.Gender;
import timetogeter.context.auth.domain.vo.Provider;
import timetogeter.context.auth.domain.vo.Role;
import timetogeter.context.group.application.dto.request.EditGroup1Request;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
import timetogeter.global.RestDocsSupport;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
class GroupDetailControllerTest extends RestDocsSupport {

    @MockBean
    private GroupManageInfoService groupManageInfoService;

    @MockBean
    private GroupManageDisplayService groupManageDisplayService;

    @MockBean
    private GroupManageMemberService groupManageMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("그룹 정보 수정 API (/api/v1/group/edit)")
    class editGroup {

        @Test
        @DisplayName("✅ 정상적으로 그룹 정보를 수정 할 수 있다.")
        @WithMockUser
        void editGroup_success() throws Exception {
            // given
            EditGroup1Request requestDto = new EditGroup1Request(
                    "a602b407-4e95-4ab1-b154-ceba94d680c2", // groupId
                    "MTIzNDU2Nzg5MDEyMzQ1Ng==",               // masterKey
                    "5만원권 모임3",                          // groupName
                    "100만원권 이미지"                        // groupImg
            );

            EditGroupInfoResponseDto responseDto = new EditGroupInfoResponseDto(
                    "a602b407-4e95-4ab1-b154-ceba94d680c2",
                    "5만원권 모임3",
                    "100만원권 이미지",
                    "xpxp_id_1",
                    List.of("manager_id_1", "xpxp_id_1")
            );

            given(groupManageInfoService.editGroup(any(EditGroup1Request.class), anyString()))
                    .willReturn(responseDto);

            RegisterUserCommand dto = new RegisterUserCommand(
                    "xpxp_id_1", "xpxp@example.com",
                    "010-1234-5678", "xpxp", Provider.GENERAL, Role.USER, "18", Gender.FEMALE
            );

            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            // when, then
            mockMvc.perform(post("/api/v1/group/edit")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.groupId").value("a602b407-4e95-4ab1-b154-ceba94d680c2"))
                    .andExpect(jsonPath("$.data.groupName").value("5만원권 모임3"))
                    .andExpect(jsonPath("$.data.groupImg").value("100만원권 이미지"))
                    .andExpect(jsonPath("$.data.managerId").value("xpxp_id_1"))
                    .andExpect(jsonPath("$.data.groupMembers[0]").value("manager_id_1"))
                    .andExpect(jsonPath("$.data.groupMembers[1]").value("xpxp_id_1"))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("그룹 정보를 수정한다")
                                            .requestFields(
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("수정할 그룹의 ID"),
                                                    fieldWithPath("personalMasterKey").type(JsonFieldType.STRING).description("사용자 마스터 키 (암호화 용도)"),
                                                    fieldWithPath("groupName").type(JsonFieldType.STRING).description("변경할 그룹 이름"),
                                                    fieldWithPath("groupImg").type(JsonFieldType.STRING).description("변경할 그룹 이미지")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("수정된 그룹 이름"),
                                                    fieldWithPath("data.groupImg").type(JsonFieldType.STRING).description("수정된 그룹 이미지"),
                                                    fieldWithPath("data.managerId").type(JsonFieldType.STRING).description("그룹 방장 ID"),
                                                    fieldWithPath("data.groupMembers").type(JsonFieldType.ARRAY).description("그룹 구성원 ID 목록")
                                            )
                                            .build()
                            )
                    ));
        }


        */
/*@Test
        @DisplayName("❌ 인증 토큰이 없으면 그룹 정보를 수정할 수 없다.")
        void editGroup_withoutToken_failure() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 그룹 내 방장이 아니면 그룹 정보를 수정할 수 없다.")
        @WithMockUser
        void editGroup_withNormalUserToken_failure() throws Exception {
            // given, when, then
        }*//*

    }

    @Nested
    @DisplayName("그룹 초대 코드 생성 API (/api/v1/group/invite)")
    class inviteGroup {

        @Test
        @DisplayName("✅ 정상적으로 그룹 초대 코드를 생성 할 수 있다.")
        @WithMockUser
        void inviteGroup_success() throws Exception {
            // given
            InviteGroupInfoRequestDto requestDto = new InviteGroupInfoRequestDto(
                    "a602b407-4e95-4ab1-b154-ceba94d680c2",
                    "OTI1NTU2Nzg5MDQ0MzQ1Ng=="
            );

            InviteGroupInfoResponseDto responseDto = new InviteGroupInfoResponseDto(
                    "http://localhost:8080/group/join?token=6VV_qbvmHFVHsuavg0kL8cw5-z9ATakBuozZsV-yLYsn-uBSEIzD_MC-Ailn4wF5M8Lg_bY40CZLEHrM5FJIl6OWDJD9n1y_wBu5zI22ldxGL7GiYdUrEIU8LpA8BWOypuZ4N5LMptXf2Q"
            );

            given(groupManageMemberService.inviteGroup(any(InviteGroupInfoRequestDto.class), anyString()))
                    .willReturn(responseDto);

            RegisterUserCommand dto = new RegisterUserCommand(
                    "xpxp_id_1", "xpxp@example.com",
                    "010-1234-5678", "xpxp", Provider.GENERAL, Role.USER, "18", Gender.FEMALE
            );

            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            // when, then
            mockMvc.perform(post("/api/v1/group/invite")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.inviteUrl").value(responseDto.inviteUrl()))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("그룹 초대 코드를 생성한다")
                                            .requestFields(
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("초대 코드를 생성할 그룹 ID"),
                                                    fieldWithPath("personalMasterKey").type(JsonFieldType.STRING).description("사용자 마스터 키 (암호화 용도)")
                                                    )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.inviteUrl").type(JsonFieldType.STRING).description("생성된 그룹 초대 URL")
                                            )
                                            .build()
                            )
                    ));
        }

       */
/* @Test
        @DisplayName("❌ 인증 토큰이 없으면 그룹 초대 코드를 생성할 수 없다.")
        void inviteGroup_withoutToken_failure() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 그룹 내 그룹멤버가 아니면 그룹 초대 코드를 생성할 수 없다.")
        @WithMockUser
        void inviteGroup_withoutGroupUserToken_failure() throws Exception {
            // given, when, then
        }*//*

    }


}*/
