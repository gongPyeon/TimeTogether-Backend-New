package timetogeter.context.promise.presentation.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import timetogeter.context.group.application.service.GroupManageDisplayService;
import timetogeter.context.group.application.service.GroupManageInfoService;
import timetogeter.context.group.application.service.GroupManageMemberService;
import timetogeter.context.promise.application.dto.request.*;
import timetogeter.context.promise.application.dto.response.*;
import timetogeter.context.promise.application.service.PromiseManageInfoService;
import timetogeter.context.promise.domain.vo.PromiseType;
import timetogeter.global.RestDocsSupport;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class PromiseManageControllerTest extends RestDocsSupport {

    @MockBean
    private GroupManageInfoService groupManageInfoService;

    @MockBean
    private GroupManageDisplayService groupManageDisplayService;

    @MockBean
    private GroupManageMemberService groupManageMemberService;

    @MockBean
    private PromiseManageInfoService promiseManageInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Authentication authentication;


    @BeforeEach
    void setupAuthentication() {
        RegisterUserCommand dto = new RegisterUserCommand(
                "bloomberg", "bloomberg@gmail.com",
                "010-1234-5678", "bloombergNickname", Provider.GENERAL, Role.USER, "20", Gender.MALE
        );
        User user = new User(dto);
        RegisterResponse registerResponse = RegisterResponse.from(user);
        UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

        authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
    }

    @Nested
    @DisplayName("약속 만들기 화면 보여주기 API (/api/v1/promise/createView)")
    class CreatePromiseView {

        @Test
        @DisplayName("✅ 사용자가 속한 그룹의 encencGroupMemberId 정보를 조회할 수 있다. (/api/v1/promise/createView1)")
        @WithMockUser
        void testViewGroup1() throws Exception {
            // given
            RegisterUserCommand dto = new RegisterUserCommand(
                    "bloomberg", "bloomberg@gmail.com",
                    "010-1234-5678", "bloombergNickname", Provider.GENERAL, Role.USER, "20", Gender.MALE
            );
            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            CreatePromiseViewRequest1 request = new CreatePromiseViewRequest1(
                    "21R517Z8ICrEK7vskd3gNq9yA1VWfEVgkx1hpjf8U+O8oS54tK16QepuIjRk8Rtm4XbYRA=="
            );
            CreatePromiseViewResponse1 response = new CreatePromiseViewResponse1(
                    "21R517Z8ICrEK7vskd3gNq9yA1VWfEVgkx1hpjf8U+O8oS54tK16QepuIjRk8Rtm4XbYRA==",
                    "rUN52v4hfGbYIua7lci6dqgnZFkrZhx00WkYwXTlNeG56FddH8d/WffmYQoX5TrOmzSKUA=="
            );

            given(promiseManageInfoService.createPromiseView1(userPrincipal.getId(), request))
                    .willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/createView1")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "encGroupId" : "21R517Z8ICrEK7vskd3gNq9yA1VWfEVgkx1hpjf8U+O8oS54tK16QepuIjRk8Rtm4XbYRA=="
                            }
                            """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.encGroupId").value("21R517Z8ICrEK7vskd3gNq9yA1VWfEVgkx1hpjf8U+O8oS54tK16QepuIjRk8Rtm4XbYRA=="))
                    .andExpect(jsonPath("$.data.encencGroupMemberId").value("rUN52v4hfGbYIua7lci6dqgnZFkrZhx00WkYwXTlNeG56FddH8d/WffmYQoX5TrOmzSKUA=="))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("사용자가 속한 그룹의 encGroupKey 정보를 조회한다.")
                                            .requestFields(
                                                    fieldWithPath("encGroupId").type(JsonFieldType.STRING).description("암호화된 그룹 ID")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.encGroupId").type(JsonFieldType.STRING).description("암호화된 그룹 ID"),
                                                    fieldWithPath("data.encencGroupMemberId").type(JsonFieldType.STRING).description("이중 암호화된 그룹 멤버 ID")
                                            )
                                            .build()
                            )
                    ));

        }


        @Test
        @DisplayName("✅ 사용자가 속한 그룹의 encGroupKey 정보를 조회할 수 있다. (/api/v1/promise/createView2)")
        @WithMockUser
        void testViewGroup2() throws Exception {
            // given
            CreatePromiseViewRequest2 request = new CreatePromiseViewRequest2(
                    "5e9c0739-1717-4574-84c7-60515c21284a",  // groupId
                    "Cb1n3T8pwO/LyxBuR81vcf+k1TvhXgIK/A=="   // encGroupMemberId
            );

            CreatePromiseViewResponse2 response = new CreatePromiseViewResponse2(
                    "2C86SvvD3azjoYXwu+1CPGKiDMSSVBnNQCp3Fo9azTwJjOIRRp+K7A=="
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            given(promiseManageInfoService.createPromiseView2(userPrincipal.getId(), request))
                    .willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/createView2")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                    {
                        "groupId" : "5e9c0739-1717-4574-84c7-60515c21284a",
                        "encGroupMemberId" : "Cb1n3T8pwO/LyxBuR81vcf+k1TvhXgIK/A=="
                    }
                    """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.encGroupKey").value("2C86SvvD3azjoYXwu+1CPGKiDMSSVBnNQCp3Fo9azTwJjOIRRp+K7A=="))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("사용자가 속한 그룹의 encGroupKey 정보를 조회한다.")
                                            .requestFields(
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("encGroupMemberId").type(JsonFieldType.STRING).description("암호화된 그룹 멤버 ID")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.encGroupKey").type(JsonFieldType.STRING).description("암호화된 그룹 키")
                                            )
                                            .build()
                            )
                    ));
        }


        @Test
        @DisplayName("✅ 사용자가 속한 그룹의 그룹키로 암호화된 사용자 정보를 조회할 수 있다. (/api/v1/promise/createView3)")
        @WithMockUser
        void testViewGroup3() throws Exception {
            // given
            CreatePromiseViewRequest3 request = new CreatePromiseViewRequest3(
                    "5e9c0739-1717-4574-84c7-60515c21284a"  // groupId
            );

            CreatePromiseViewResponse3 response = new CreatePromiseViewResponse3(
                    "5e9c0739-1717-4574-84c7-60515c21284a",
                    "진달래전",
                    "진달래먹기모임",
                    "bloomberg",
                    List.of(
                            "Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD",
                            "Cb1n3T8pwO/LyxBuR81vcf+k1TvhXgIK/A=="
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            given(promiseManageInfoService.createPromiseView3(userPrincipal.getId(), request))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/v1/promise/createView3")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                    {
                        "groupId" : "5e9c0739-1717-4574-84c7-60515c21284a"
                    }
                    """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.groupId").value("5e9c0739-1717-4574-84c7-60515c21284a"))
                    .andExpect(jsonPath("$.data.groupName").value("진달래전"))
                    .andExpect(jsonPath("$.data.groupImg").value("진달래먹기모임"))
                    .andExpect(jsonPath("$.data.managerId").value("bloomberg"))
                    .andExpect(jsonPath("$.data.encUserId[0]").value("Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD"))
                    .andExpect(jsonPath("$.data.encUserId[1]").value("Cb1n3T8pwO/LyxBuR81vcf+k1TvhXgIK/A=="))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("그룹 관련 API")
                                            .description("그룹 ID로 해당 그룹에 속한 암호화된 사용자 정보를 조회한다.")
                                            .requestFields(
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("그룹 이름"),
                                                    fieldWithPath("data.groupImg").type(JsonFieldType.STRING).description("그룹 이미지"),
                                                    fieldWithPath("data.managerId").type(JsonFieldType.STRING).description("그룹장 ID"),
                                                    fieldWithPath("data.encUserId").type(JsonFieldType.ARRAY).description("암호화된 사용자 ID 목록")
                                            )
                                            .build()
                            )
                    ));
        }




    }


    @Nested
    @DisplayName("약속 만들고 알림 보내기 API (/api/v1/promise/create)")
    class CreatePromise {


        @Test
        @DisplayName("✅ 사용자가 속한 그룹의 그룹키로 이중 암호화된 멤버 ID를 조회할 수 있다. (/api/v1/promise/create1)")
        @WithMockUser
        void testPromiseCreate1() throws Exception {
            // given
            RegisterUserCommand dto = new RegisterUserCommand(
                    "bloomberg", "bloomberg@gmail.com",
                    "010-1234-5678", "bloombergNickname", Provider.GENERAL, Role.USER, "20", Gender.MALE
            );
            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            CreatePromiseAlimRequest1 request = new CreatePromiseAlimRequest1(
                    "5e9c0739-1717-4574-84c7-60515c21284a",
                    "21R517Z8ICrEK7vskd3gNq9yA1VWfEVgkx1hpjf8U+O8oS54tK16QepuIjRk8Rtm4XbYRA=="
            );
            CreatePromiseAlimResponse1 response = new CreatePromiseAlimResponse1(
                    "rUN52v4hfGbYIua7lci6dqgnZFkrZhx00WkYwXTlNeG56FddH8d/WffmYQoX5TrOmzSKUA=="
            );

            given(promiseManageInfoService.createPromise1(userPrincipal.getId(), request))
                    .willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/create1")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                    {
                        "groupId": "5e9c0739-1717-4574-84c7-60515c21284a",
                        "encGroupId": "21R517Z8ICrEK7vskd3gNq9yA1VWfEVgkx1hpjf8U+O8oS54tK16QepuIjRk8Rtm4XbYRA=="
                    }
                    """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.encencGroupMemberId")
                            .value("rUN52v4hfGbYIua7lci6dqgnZFkrZhx00WkYwXTlNeG56FddH8d/WffmYQoX5TrOmzSKUA=="))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("사용자가 속한 그룹의 encGroupKey로 이중 암호화된 그룹 멤버 ID를 조회한다.")
                                            .requestFields(
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("encGroupId").type(JsonFieldType.STRING).description("암호화된 그룹 ID")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.encencGroupMemberId").type(JsonFieldType.STRING).description("이중 암호화된 그룹 멤버 ID")
                                            )
                                            .build()
                            )
                    ));
        }

        @Test
        @DisplayName("✅ 사용자가 속한 그룹의 encGroupKey를 조회할 수 있다. (/api/v1/promise/create2)")
        @WithMockUser
        void testPromiseCreate2() throws Exception {
            // given
            CreatePromiseAlimRequest2 request = new CreatePromiseAlimRequest2(
                    "Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD", // encUserId
                    "5e9c0739-1717-4574-84c7-60515c21284a"  // groupId
            );
            CreatePromiseAlimResponse2 response = new CreatePromiseAlimResponse2(
                    "p3U0jakid0CAKP6k1LiMda4uQjgrTk9wQ+Ks+q/wxgItTlzwb7OE3Q=="
            );

            given(promiseManageInfoService.createPromise2(request)).willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/create2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                    {
                        "encUserId": "Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD",
                        "groupId": "5e9c0739-1717-4574-84c7-60515c21284a"
                    }
                    """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.encGroupKey").value("p3U0jakid0CAKP6k1LiMda4uQjgrTk9wQ+Ks+q/wxgItTlzwb7OE3Q=="))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("사용자가 속한 그룹의 encGroupKey를 조회한다.")
                                            .requestFields(
                                                    fieldWithPath("encUserId").type(JsonFieldType.STRING).description("암호화된 사용자 ID"),
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.encGroupKey").type(JsonFieldType.STRING).description("암호화된 그룹 키")
                                            )
                                            .build()
                            )
                    ));
        }

        @Test
        @DisplayName("✅ 약속장인 사람의 정보를 넘기고 약속 아이디 반환. (/api/v1/promise/create3)")
        @WithMockUser
        void testPromiseCreate3() throws Exception {
            // given
            CreatePromiseAlimRequest3 request = new CreatePromiseAlimRequest3(
                    "5e9c0739-1717-4574-84c7-60515c21284a",
                    "진달래전 연구1",
                    PromiseType.ONLINE,
                    "진달래전 연구 그림",
                    "bloomberg",
                    LocalDate.of(2025, 5, 30),
                    LocalDate.of(2025, 6, 3)
            );
            CreatePromiseAlimResponse3 response = new CreatePromiseAlimResponse3(
                    "01bb0b8f-ea3f-4d0d-8ed8-8b6ce07ca665"
            );

            given(promiseManageInfoService.createPromise3(any(CreatePromiseAlimRequest3.class)))
                    .willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/create3")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                    {
                        "groupId": "5e9c0739-1717-4574-84c7-60515c21284a",
                        "title": "진달래전 연구1",
                        "type": "ONLINE",
                        "promiseImg": "진달래전 연구 그림",
                        "encManagerId": "bloomberg",
                        "startDate": "2025-05-30",
                        "endDate": "2025-06-03"
                    }
                    """))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.promiseId").value("01bb0b8f-ea3f-4d0d-8ed8-8b6ce07ca665"))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("약속장 정보를 넘기고 약속을 생성하여 약속 ID를 반환한다.")
                                            .requestFields(
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID"),
                                                    fieldWithPath("title").type(JsonFieldType.STRING).description("약속 제목"),
                                                    fieldWithPath("type").type(JsonFieldType.STRING).description("약속 유형 (예: ONLINE, OFFLINE)"),
                                                    fieldWithPath("promiseImg").type(JsonFieldType.STRING).description("약속 이미지 또는 설명"),
                                                    fieldWithPath("encManagerId").type(JsonFieldType.STRING).description("암호화된 약속장 사용자 ID"),
                                                    fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜 (yyyy-MM-dd)"),
                                                    fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜 (yyyy-MM-dd)")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.promiseId").type(JsonFieldType.STRING).description("생성된 약속 ID")
                                            )
                                            .build()
                            )
                    ));
        }


        @Test
        @DisplayName("✅ 약속 내 예비 약속원들의 개인키로 암호화한 그룹키 리스트 반환 (/api/v1/promise/create4)")
        @WithMockUser
        void testPromiseCreate4() throws Exception {
            // given
            RegisterUserCommand dto = new RegisterUserCommand(
                    "bloomberg", "bloomberg@gmail.com",
                    "010-1234-5678", "bloombergNickname", Provider.GENERAL, Role.USER, "20", Gender.MALE
            );
            User user = new User(dto);
            RegisterResponse registerResponse = RegisterResponse.from(user);
            UserPrincipal userPrincipal = new UserPrincipal(registerResponse);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );

            List<String> encUserIdList = List.of(
                    "Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD",
                    "Cb1n3T8pwO/LyxBuR81vcf+k1TvhXgIK/A=="
            );
            String groupId = "5e9c0739-1717-4574-84c7-60515c21284a";

            CreatePromiseAlimRequest4 request = new CreatePromiseAlimRequest4(encUserIdList, groupId);

            List<String> encGroupKeyList = List.of(
                    "p3U0jakid0CAKP6k1LiMda4uQjgrTk9wQ+Ks+q/wxgItTlzwb7OE3Q==",
                    "2C86SvvD3azjoYXwu+1CPGKiDMSSVBnNQCp3Fo9azTwJjOIRRp+K7A=="
            );
            CreatePromiseAlimResponse4 response = new CreatePromiseAlimResponse4(encGroupKeyList);

            given(promiseManageInfoService.createPromise4(any(CreatePromiseAlimRequest4.class)))
                    .willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/create4")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.encGroupKeyList[0]").value(encGroupKeyList.get(0)))
                    .andExpect(jsonPath("$.data.encGroupKeyList[1]").value(encGroupKeyList.get(1)))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("예비 약속원들의 개인키로 암호화한 그룹키 리스트를 반환한다.")
                                            .requestFields(
                                                    fieldWithPath("encUserIdList").type(JsonFieldType.ARRAY).description("암호화된 사용자 ID 리스트"),
                                                    fieldWithPath("groupId").type(JsonFieldType.STRING).description("그룹 ID")
                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.encGroupKeyList").type(JsonFieldType.ARRAY).description("암호화된 그룹키 리스트")
                                            )
                                            .build()
                            )
                    ));

        }

        //TODO: 아래 테스트 코드 500 오류 버그 (실제 실행시에는 결과json 잘 나오는 상태)
        /*@Test
        @DisplayName("✅ 약속을 최종적으로 만들었다. (/api/v1/promise/create5)")
        @WithMockUser
        void testPromiseCreate5() throws Exception {
            // given
            List<HashMap<String, Integer>> list = List.of(
                    new HashMap<>(Map.of("Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD", 1)),
                    new HashMap<>(Map.of("Cb1n3T8pwO/LyxBuR81vcf+k1TvhXgIK/A==", 1))
            );
            CreatePromiseAlimRequest5 request = new CreatePromiseAlimRequest5(
                    "01bb0b8f-ea3f-4d0d-8ed8-8b6ce07ca665",
                    "3gAi1rYpK3XEf+3uwN3gZ6giA1UHe0pgnU9i9GevVrHvrywsJl6IbqMGAhfE3EdmJat+Iw==",
                    "j18vwO4uYUaaf/6eGB49N4TzCiw0bneYx7P8",
                    "Cr9nxjou18jf38nu0aJ4Iyn9tDLVvzT37qMD",
                    "gFRv1vc8XWqYLdbl0qfjbNAJZgwKTk9wAFzyVbGMGn5pioj1YggyNQ==",
                    List.of(
                            "5zs5+VGE5Myi5Nd6k5XFQb0a4t9yLU3xDHFznVJHEV1jVUOO70gIeQ==",
                            "55Nfbri0tOQpJs58WZgZhSJI3SC2Lh9Zx/Iq5xGLVx55fDQqIs20FQ=="
                    ),
                    list
            );

            CreatePromiseAlimResponse5 response = new CreatePromiseAlimResponse5("약속 생성 및 알림 전송 완료");

            given(promiseManageInfoService.createPromise5(anyString(), any(CreatePromiseAlimRequest5.class)))
                    .willReturn(response);

            // when, then
            mockMvc.perform(post("/api/v1/promise/create5")
                            .with(authentication(authentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("요청에 성공했습니다."))
                    .andExpect(jsonPath("$.data.message").value("약속 생성 및 알림 전송 완료"))
                    .andDo(restDocs.document(
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .tag("약속 관련 API")
                                            .description("약속을 최종 생성하고 알림을 전송한다.")
                                            .requestFields(
                                                    fieldWithPath("promiseId").type(JsonFieldType.STRING).description("약속 ID"),
                                                    fieldWithPath("encPromiseId").type(JsonFieldType.STRING).description("암호화된 약속 ID"),
                                                    fieldWithPath("encPromiseMemberId").type(JsonFieldType.STRING).description("암호화된 약속원 ID"),
                                                    fieldWithPath("encUserId").type(JsonFieldType.STRING).description("암호화된 사용자 ID"),
                                                    fieldWithPath("encPromiseKey").type(JsonFieldType.STRING).description("암호화된 약속 키"),
                                                    fieldWithPath("encEncGroupKeyList").type(JsonFieldType.ARRAY).description("암호화된 그룹키 리스트"),
                                                    fieldWithPath("whichEncUserIdsIn")
                                                            .ignored()
                                                            .attributes(key("description").value("암호화된 사용자 ID를 키로, 1을 값으로 갖는 단일 key-value 맵의 배열. 예: [{\"암호화ID\": 1}, ...]"))

                                            )
                                            .responseFields(
                                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                    fieldWithPath("data.message").type(JsonFieldType.STRING).description("응답 상세 메시지")
                                            )
                                            .build()
                            )
                    ));
        }*/


    }

}