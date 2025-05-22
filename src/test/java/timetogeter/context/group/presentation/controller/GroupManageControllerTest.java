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
import timetogeter.context.group.application.dto.request.CreateGroup1Request;
import timetogeter.context.group.application.dto.request.JoinGroupInnerRequestDto;
import timetogeter.context.group.application.dto.request.JoinGroupRequestDto;
import timetogeter.context.group.application.dto.request.ViewGroupsInRequestDto;
import timetogeter.context.group.application.dto.response.CreateGroup2Response;
import timetogeter.context.group.application.dto.response.JoinGroupResponseDto;
import timetogeter.context.group.application.dto.response.ViewGroupsInResponseDto;
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
import timetogeter.global.RestDocsSupport;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class GroupManageControllerTest extends RestDocsSupport {

    @MockBean
    private GroupManageInfoService groupManageInfoService;

    @MockBean
    private GroupManageDisplayService groupManageDisplayService;

    @MockBean
    private GroupManageMemberService groupManageMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("그룹 생성 API (/api/v1/group/new)")
    class CreateGroup {

        @Test
        @DisplayName("✅ 정상적으로 그룹을 생성할 수 있다.")
        void createGroup_success() throws Exception {
            // given
            CreateGroup1Request createGroupRequestDto = new CreateGroup1Request(
                    "5만원권을 사랑하는 모임",
                    "5만원권 묶음",
                    "OTI1NTU2Nzg5MDQ0MzQ1Ng=="
            );

            RegisterUserCommand dto = new RegisterUserCommand(
                    "ImManager",  "immanager@example.com",
                    "010-1234-5678", "ImManager",Provider.GENERAL, Role.USER, "25", Gender.FEMALE
            );

            User user = new User(dto);

            RegisterResponse registerResponse = RegisterResponse.from(user);

            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            // when
            given(groupManageInfoService.createGroup(createGroupRequestDto, userPrincipal.getId().toString())).willReturn(
                    new CreateGroup2Response(
                            "16f3f99e-fc2f-43a9-a8e5-83bc3e5ab793",
                            createGroupRequestDto.groupName(),
                            createGroupRequestDto.groupImg(),
                            userPrincipal.getId().toString()
                    )
            );
            // then
            mockMvc.perform(post("/api/v1/group/new")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createGroupRequestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.groupId").value("16f3f99e-fc2f-43a9-a8e5-83bc3e5ab793"))
                    .andExpect(jsonPath("$.data.groupName").value("5만원권을 사랑하는 모임"))
                    .andExpect(jsonPath("$.data.groupImg").value("5만원권 묶음"))
                    .andExpect(jsonPath("$.data.managerId").value(userPrincipal.getId()))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("그룹 생성 성공")
                                            .requestFields(
                                                    fieldWithPath("groupName").type(JsonFieldType.STRING).description("그룹 이름"),
                                                    fieldWithPath("groupImg").type(JsonFieldType.STRING).description("그룹 이미지"),
                                                    fieldWithPath("personalMasterKey").type(JsonFieldType.STRING).description("마스터 키")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.groupId").type(JsonFieldType.STRING).description("생성된 그룹 아이디"),
                                                    fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("그룹 이름"),
                                                    fieldWithPath("data.groupImg").type(JsonFieldType.STRING).description("그룹 이미지"),
                                                    fieldWithPath("data.managerId").type(JsonFieldType.STRING).description("그룹을 만든 방장 ID")
                                            )
                                            .build()
                            )
                    ));
        }


        @Test
        @DisplayName("❌ 인증 토큰이 없으면 그룹을 생성할 수 없다.")
        void createGroup_withoutToken_failure() throws Exception {
            CreateGroup1Request createGroupRequestDto = new CreateGroup1Request(
                    "5만원권을 사랑하는 모임",
                    "5만원권 묶음",
                    "OTI1NTU2Nzg5MDQ0MzQ1Ng=="
            );

            mockMvc.perform(post("/api/v1/group/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createGroupRequestDto))
                    )
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    //.andExpect(jsonPath("$.code").value(403))
                    //.andExpect(jsonPath("$.message").value("서버 내부 오류입니다!"))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("인증 토큰 없을 때 그룹 생성 실패")
                                            .responseFields(
                                                    //fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드, 403"),
                                                    //fieldWithPath("message").type(JsonFieldType.STRING).description("오류 메시지, 인증 오류임을 알림")
                                            )
                                            .build()
                            )
                    ));

        }


    }

    @Nested
    @DisplayName("그룹 목록 조회 API (/api/v1/group/view)")
    class ViewGroupList {

        @Test
        @DisplayName("✅ 사용자가 속한 그룹 목록을 정상적으로 조회할 수 있다.")
        @WithMockUser
        void viewGroupList_success() throws Exception {
            // given
            ViewGroupsInRequestDto requestDto = new ViewGroupsInRequestDto(
                   "MTIzNDU2Nzg5MDEyMzQ1Ng=="
            );

            List<ViewGroupsInResponseDto> responseList = List.of(
                    new ViewGroupsInResponseDto(
                            "16f3f99e-fc2f-43a9-a8e5-83bc3e5ab793",
                            "피크닉",
                            "피크닉_이미지",
                            List.of("manager_id_1", "ImManager")
                    ),
                    new ViewGroupsInResponseDto(
                            "a602b407-4e95-4ab1-b154-ceba94d680c2",
                            "5만원권모임",
                            "5만원권_이미지",
                            List.of("manager_id_1", "ImManager")
                    )
            );

            given(groupManageDisplayService.viewGroupsIn(any(ViewGroupsInRequestDto.class), anyString()))
                    .willReturn(responseList);

            RegisterUserCommand dto = new RegisterUserCommand(
                    "ImManager",  "immanager@example.com",
                    "010-1234-5678", "ImManager",Provider.GENERAL, Role.USER, "25", Gender.FEMALE
            );
            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            // when, then
            mockMvc.perform(post("/api/v1/group/view")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].groupId").value("16f3f99e-fc2f-43a9-a8e5-83bc3e5ab793"))
                    .andExpect(jsonPath("$.data[0].groupName").value("피크닉"))
                    .andExpect(jsonPath("$.data[0].groupImg").value("피크닉_이미지"))
                    .andExpect(jsonPath("$.data[0].groupMembers[0]").value("manager_id_1"))
                    .andExpect(jsonPath("$.data[0].groupMembers[1]").value("ImManager"))
                    .andExpect(jsonPath("$.data[1].groupId").value("a602b407-4e95-4ab1-b154-ceba94d680c2"))
                    .andExpect(jsonPath("$.data[1].groupName").value("5만원권모임"))
                    .andExpect(jsonPath("$.data[1].groupImg").value("5만원권_이미지"))
                    .andExpect(jsonPath("$.data[1].groupMembers[0]").value("manager_id_1"))
                    .andExpect(jsonPath("$.data[1].groupMembers[1]").value("ImManager"))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("사용자가 속한 그룹 목록 조회 성공")
                                            .requestFields(
                                                    fieldWithPath("personalMasterKey").type(JsonFieldType.STRING).optional().description("마스터키")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data[].groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("data[].groupName").type(JsonFieldType.STRING).description("그룹 이름"),
                                                    fieldWithPath("data[].groupImg").type(JsonFieldType.STRING).description("그룹 이미지"),
                                                    fieldWithPath("data[].groupMembers").type(JsonFieldType.ARRAY).description("그룹 멤버 ID 목록")
                                            )
                                            .build()
                            )
                    ));
        }

        /*@Test
        @DisplayName("❌ 인증 토큰이 없으면 그룹 목록을 조회할 수 없다.")
        void viewGroupList_withoutToken_failure() throws Exception {
            // given, when, then
        }

        @Test
        @DisplayName("❌ 유효하지 않은 마스터 키로 그룹 목록을 조회할 수 없다.")
        @WithMockUser
        void viewGroupList_invalidMasterKey_failure() throws Exception {
            // given, when, then
        }*/
    }

    @Nested
    @DisplayName("그룹 초대수락 API (/api/v1/group/join)")
    class JoinGroup {

        @Test
        @DisplayName("✅ 초대받은 사용자가 그룹에 정상적으로 참여할 수 있다.")
        @WithMockUser
        void joinGroup_success() throws Exception {
            // given
            JoinGroupRequestDto requestDto = new JoinGroupRequestDto(
                    "6VV_qbvmHFVHsuavg0kL8cw5-z9ATakBuozZsV-yLYsn-uBSEIzD_MC-Ailn4wF5M8Lg_bY40CZLEHrM5FJIl6OWDJD9n1y_wBu5zI22ldxGL7GiYdUrEIU8LpA8BWOypuZ4N5LMptXf2Q",
                    "MTIzNDU2Nzg5MDEyMzQ1Ng=="
            );

            JoinGroupResponseDto responseDto = new JoinGroupResponseDto(
                    "a602b407-4e95-4ab1-b154-ceba94d680c2",
                    "피크닉",
                    "피크닉 이미지",
                    "nanana",
                    3L

            );

            given(groupManageMemberService.getRequestDto(any(JoinGroupRequestDto.class)))
                    .willReturn(mock(JoinGroupInnerRequestDto.class));
            given(groupManageMemberService.joinGroup(any(JoinGroupInnerRequestDto.class), anyString()))
                    .willReturn(responseDto);

            RegisterUserCommand dto = new RegisterUserCommand(
                    "nanana", "nanana@example.com",
                    "010-1234-5678", "nanana", Provider.GENERAL, Role.USER, "18", Gender.FEMALE
            );
            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            // when, then
            mockMvc.perform(post("/api/v1/group/join")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.groupId").value("a602b407-4e95-4ab1-b154-ceba94d680c2"))
                    .andExpect(jsonPath("$.data.groupName").value("피크닉"))
                    .andExpect(jsonPath("$.data.groupImg").value("피크닉 이미지"))
                    .andExpect(jsonPath("$.data.invitedId").value("nanana"))
                    .andExpect(jsonPath("$.data.groupPeopleNum").value(3L))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("초대받은 사용자가 그룹에 정상적으로 참여")
                                            .requestFields(
                                                    fieldWithPath("token").type(JsonFieldType.STRING).description("초대 코드"),
                                                    fieldWithPath("personalMasterKey").type(JsonFieldType.STRING).description("사용자의 개인 마스터키")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("그룹 이름"),
                                                    fieldWithPath("data.groupImg").type(JsonFieldType.STRING).description("그룹 이미지"),
                                                    fieldWithPath("data.invitedId").type(JsonFieldType.STRING).description("초대받은 사용자 ID"),
                                                    fieldWithPath("data.groupPeopleNum").type(JsonFieldType.NUMBER).description("그룹 인원 수")
                                            )
                                            .build()
                            )
                    ));
        }


        /*@Test
        @DisplayName("❌ 인증 없이 초대 수락 시 실패한다.")
        void joinGroup_withoutToken_failure() throws Exception {
            // given, when, then
        }*/
    }
}

