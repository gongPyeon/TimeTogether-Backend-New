//package timetogeter.context.group.presentation.controller;
//
//import com.epages.restdocs.apispec.ResourceSnippetParameters;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import timetogeter.context.auth.application.dto.RegisterResponse;
//import timetogeter.context.auth.application.dto.RegisterUserCommand;
//import timetogeter.context.auth.domain.adaptor.UserPrincipal;
//import timetogeter.context.auth.domain.entity.User;
//import timetogeter.context.auth.domain.vo.Gender;
//import timetogeter.context.auth.domain.vo.Provider;
//import timetogeter.context.auth.domain.vo.Role;
//import timetogeter.context.group.application.dto.request.*;
//import timetogeter.context.group.application.dto.response.*;
//import timetogeter.context.group.application.service.GroupManageDisplayService;
//import timetogeter.context.group.application.service.GroupManageInfoService;
//import timetogeter.context.group.application.service.GroupManageMemberService;
//import timetogeter.global.RestDocsSupport;
//
//import java.util.List;
//
//import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@ActiveProfiles("test")
//class GroupDetailControllerTest extends RestDocsSupport {
//
//
//    @MockBean
//    private GroupManageInfoService groupManageInfoService;
//
//    @MockBean
//    private GroupManageDisplayService groupManageDisplayService;
//
//    @MockBean
//    private GroupManageMemberService groupManageMemberService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Authentication authentication;
//
//
//    @BeforeEach
//    void setupAuthentication() {
//        RegisterUserCommand dto = new RegisterUserCommand(
//                "xpxp_id_1", "xpxp@example.com",
//                "010-1234-5678", "xpxp", Provider.GENERAL, Role.USER, "18", Gender.FEMALE
//        );
//        User user = new User(dto);
//        RegisterResponse registerResponse = RegisterResponse.from(user);
//        UserPrincipal userPrincipal = new UserPrincipal(registerResponse);
//
//        authentication = new UsernamePasswordAuthenticationToken(
//                userPrincipal, null, userPrincipal.getAuthorities()
//        );
//    }
//
//    @Nested
//    @DisplayName("사용자의 그룹 정보 수정 API (/api/v1/group/edit)")
//    class EditGroup {
//
//        @Test
//        @DisplayName("✅ 방장이 그룹 정보를 수정할 수 있다 (/api/v1/group/edit1)")
//        @WithMockUser
//        void testEditGroup1() throws Exception {
//            // given
//            String encencGroupMemberId = "aIswNz3D7j0HvvgUOzP8I5ILDJt2Y76dI8LgiBnV6e4YbM0vyc++dZSNG7Le7oIuRZ7UGw==";
//            EditGroup1Response response = new EditGroup1Response(encencGroupMemberId);
//            given(groupManageInfoService.editGroup1(any(EditGroup1Request.class), anyString()))
//                    .willReturn(response);
//
//            // when & then
//            mockMvc.perform(post("/api/v1/group/edit1")
//                            .with(authentication(authentication))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("""
//                {
//                    "groupId": "333571d9-a517-4e7f-94a3-d71aba508940",
//                    "encGroupId": "HcFINyWax1NGlY99f3b4M81aFPIveL7Uct7k2j3wq6pSFMQi76jivIPVPGJoxbT6l2QCCQ==",
//                    "groupName": "toefl(수정됨제목)",
//                    "groupImg": "toefl(수정됨이미지)",
//                    "description": ""
//                }
//            """))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(200))
//                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
//                    .andExpect(jsonPath("$.data.encencGroupMemberId").value(encencGroupMemberId))
//                    .andDo(restDocs.document(
//                            resource(
//                                    ResourceSnippetParameters.builder()
//                                            .tag("그룹 관련 API")
//                                            .description("방장이 그룹 정보를 수정하고, encencGroupMemberId를 반환받는 API")
//                                            .requestFields(
//                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID (UUID)"),
//                                                    fieldWithPath("encGroupId").type(JsonFieldType.STRING).description("암호화된 그룹 ID"),
//                                                    fieldWithPath("groupName").type(JsonFieldType.STRING).description("변경할 그룹 이름"),
//                                                    fieldWithPath("groupImg").type(JsonFieldType.STRING).description("변경할 그룹 이미지"),
//                                                    fieldWithPath("description").type(JsonFieldType.STRING).description("변경할 그룹 설명")
//                                            )
//                                            .responseFields(
//                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
//                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                                    fieldWithPath("data.encencGroupMemberId").type(JsonFieldType.STRING).description("이중 암호화된 그룹 멤버 ID")
//                                            )
//                                            .build()
//                            )
//                    ));
//        }
//
//
//        @Test
//        @DisplayName("✅ 방장이 그룹 정보를 수정할 수 있다 (/api/v1/group/edit2)")
//        @WithMockUser
//        void testEditGroup2() throws Exception {
//            // given
//            EditGroup2Response response = new EditGroup2Response(
//                    "faYZeiGexxgysO4hImz7ALhoY7lNfrDEiC5hgfyjywe4B0TDqTFSmQ=="
//            );
//
//            given(groupManageInfoService.editGroup2(any(EditGroup2Request.class)))
//                    .willReturn(response);
//
//            // when & then
//            mockMvc.perform(post("/api/v1/group/edit2")
//                            .with(authentication(authentication))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("""
//                        {
//                            "groupId": "333571d9-a517-4e7f-94a3-d71aba508940",
//                            "encUserId": "FyK5/hMWlJBXsh0uh75Pmz3d5+53FDwtrA=="
//                        }
//                    """))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(200))
//                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
//                    .andExpect(jsonPath("$.data.encGroupKey").value("faYZeiGexxgysO4hImz7ALhoY7lNfrDEiC5hgfyjywe4B0TDqTFSmQ=="))
//                    .andDo(restDocs.document(
//                            resource(
//                                    ResourceSnippetParameters.builder()
//                                            .tag("그룹 관련 API")
//                                            .description("encUserId로 그룹 키(encGroupKey)를 조회하는 API")
//                                            .requestFields(
//                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID (UUID)"),
//                                                    fieldWithPath("encUserId").type(JsonFieldType.STRING).description("사용자의 암호화된 ID")
//                                            )
//                                            .responseFields(
//                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
//                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                                    fieldWithPath("data.encGroupKey").type(JsonFieldType.STRING).description("암호화된 그룹 키")
//                                            )
//                                            .build()
//                            )
//                    ));
//        }
//
//        @Test
//        @DisplayName("✅ 방장이 그룹 정보를 수정할 수 있다 (/api/v1/group/edit3)")
//        @WithMockUser
//        void testEditGroup3() throws Exception {
//            // given
//            EditGroup3Response response = new EditGroup3Response(
//                    "toefl(수정됨제목)",
//                    "",
//                    "toefl(수정됨이미지)",
//                    List.of("FyK5/hMWlJBXsh0uh75Pmz3d5+53FDwtrA==")
//            );
//
//            given(groupManageInfoService.editGroup3(any(EditGroup3Request.class)))
//                    .willReturn(response);
//
//            // when & then
//            mockMvc.perform(post("/api/v1/group/edit3")
//                            .with(authentication(authentication))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("""
//                        {
//                            "groupId": "333571d9-a517-4e7f-94a3-d71aba508940"
//                        }
//                    """))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(200))
//                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
//                    .andExpect(jsonPath("$.data.groupName").value("toefl(수정됨제목)"))
//                    .andExpect(jsonPath("$.data.groupExplain").value(""))
//                    .andExpect(jsonPath("$.data.groupImg").value("toefl(수정됨이미지)"))
//                    .andExpect(jsonPath("$.data.encUserIdList").isArray())
//                    .andExpect(jsonPath("$.data.encUserIdList[0]").value("FyK5/hMWlJBXsh0uh75Pmz3d5+53FDwtrA=="))
//                    .andDo(restDocs.document(
//                            resource(
//                                    ResourceSnippetParameters.builder()
//                                            .tag("그룹 관련 API")
//                                            .description("그룹 아이디로 그룹 정보 및 암호화된 사용자 ID 리스트 조회")
//                                            .requestFields(
//                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID (UUID)")
//                                            )
//                                            .responseFields(
//                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
//                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                                    fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("그룹 이름"),
//                                                    fieldWithPath("data.groupExplain").type(JsonFieldType.STRING).description("그룹 설명"),
//                                                    fieldWithPath("data.groupImg").type(JsonFieldType.STRING).description("그룹 이미지 URL 또는 이름"),
//                                                    fieldWithPath("data.encUserIdList[]").type(JsonFieldType.ARRAY).description("암호화된 사용자 ID 리스트")
//                                            )
//                                            .build()
//                            )
//                    ));
//        }
//
//
//    }
//
//    @Nested
//    @DisplayName("그룹원의 그룹 초대하기 API (/api/v1/group/invite)")
//    class InviteGroup {
//
//        @Test
//        @DisplayName("✅ 그룹원이 초대코드를 발급한다 (/api/v1/group/invite1)")
//        @WithMockUser
//        void testInviteGroup1() throws Exception {
//            // given
//            InviteGroup1Response response = new InviteGroup1Response(
//                    "aIswNz3D7j0HvvgUOzP8I5ILDJt2Y76dI8LgiBnV6e4YbM0vyc++dZSNG7Le7oIuRZ7UGw=="
//            );
//
//            String userId = "xpxp_id_1"; // 실제 UserPrincipal.getId()와 매칭되는 값
//
//            given(groupManageMemberService.inviteGroup1(any(InviteGroup1Request.class), eq(userId)))
//                    .willReturn(response);
//
//            String requestBody = """
//            {
//                "groupId": "333571d9-a517-4e7f-94a3-d71aba508940",
//                "encGroupId": "HcFINyWax1NGlY99f3b4M81aFPIveL7Uct7k2j3wq6pSFMQi76jivIPVPGJoxbT6l2QCCQ=="
//            }
//            """;
//
//            // when, then
//            mockMvc.perform(post("/api/v1/group/invite1")
//                            .with(authentication(authentication))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestBody))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(200))
//                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
//                    .andExpect(jsonPath("$.data.encencGroupMemberId").value("aIswNz3D7j0HvvgUOzP8I5ILDJt2Y76dI8LgiBnV6e4YbM0vyc++dZSNG7Le7oIuRZ7UGw=="))
//                    .andDo(restDocs.document(
//                            resource(
//                                    ResourceSnippetParameters.builder()
//                                            .tag("그룹 관련 API")
//                                            .description("그룹원이 그룹 초대코드를 발급한다")
//                                            .requestFields(
//                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID (UUID)"),
//                                                    fieldWithPath("encGroupId").type(JsonFieldType.STRING).description("암호화된 그룹 ID")
//                                            )
//                                            .responseFields(
//                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
//                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                                    fieldWithPath("data.encencGroupMemberId").type(JsonFieldType.STRING).description("이중 암호화된 그룹 멤버 ID")
//                                            )
//                                            .build()
//                            )
//                    ));
//        }
//
//        @Test
//        @DisplayName("✅ 그룹원이 초대코드를 발급한다 (/api/v1/group/invite2)")
//        @WithMockUser
//        void testInviteGroup2() throws Exception {
//            // given
//            InviteGroup2Response response = new InviteGroup2Response(
//                    "faYZeiGexxgysO4hImz7ALhoY7lNfrDEiC5hgfyjywe4B0TDqTFSmQ=="
//            );
//
//            // Mocking service method
//            given(groupManageMemberService.inviteGroup2(any(InviteGroup2Request.class)))
//                    .willReturn(response);
//
//            String requestBody = """
//            {
//                "groupId": "333571d9-a517-4e7f-94a3-d71aba508940",
//                "encUserId": "FyK5/hMWlJBXsh0uh75Pmz3d5+53FDwtrA=="
//            }
//            """;
//
//            // when, then
//            mockMvc.perform(post("/api/v1/group/invite2")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestBody))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(200))
//                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
//                    .andExpect(jsonPath("$.data.encGroupKey").value("faYZeiGexxgysO4hImz7ALhoY7lNfrDEiC5hgfyjywe4B0TDqTFSmQ=="))
//                    .andDo(restDocs.document(
//                            resource(
//                                    ResourceSnippetParameters.builder()
//                                            .tag("그룹 관련 API")
//                                            .description("그룹원이 encUserId와 groupId로 encGroupKey를 조회한다")
//                                            .requestFields(
//                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID (UUID)"),
//                                                    fieldWithPath("encUserId").type(JsonFieldType.STRING).description("암호화된 사용자 ID")
//                                            )
//                                            .responseFields(
//                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
//                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                                    fieldWithPath("data.encGroupKey").type(JsonFieldType.STRING).description("암호화된 그룹 키")
//                                            )
//                                            .build()
//                            )
//                    ));
//        }
//
//        @Test
//        @DisplayName("✅ 그룹원이 초대코드를 발급한다 (/api/v1/group/invite3)")
//        @WithMockUser(username = "xpxp")
//        void testInviteGroup3() throws Exception {
//            // given
//            String expectedResponse = "발급하신 초대코드의 유효기한 : 60분";
//            given(groupManageMemberService.inviteGroup3(any(InviteGroup3Request.class)))
//                    .willReturn(expectedResponse);
//
//            String requestBody = """
//    {
//        "randomKeyForRedis": "d0205893-8fa4-46b4-b030-84d1fa3e3c7e"
//    }
//    """;
//
//            // when, then
//            mockMvc.perform(post("/api/v1/group/invite3")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(requestBody))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.code").value(200))
//                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
//                    .andExpect(jsonPath("$.data").value(expectedResponse))
//                    .andDo(restDocs.document(
//                            resource(
//                                    ResourceSnippetParameters.builder()
//                                            .tag("그룹 관련 API")
//                                            .description("그룹원이 초대코드를 발급한다")
//                                            .requestFields(
//                                                    fieldWithPath("randomKeyForRedis").type(JsonFieldType.STRING).description("Redis에 저장할 랜덤 UUID 키")
//                                            )
//                                            .responseFields(
//                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드 (200)"),
//                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                                    fieldWithPath("data").type(JsonFieldType.STRING).description("초대코드 유효기간 안내 메시지")
//                                            )
//                                            .build()
//                            )
//                    ));
//        }
//
//
//    }
//
//
//}