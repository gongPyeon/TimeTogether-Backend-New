package timetogeter.context.auth.presentation.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import timetogeter.global.RestDocsSupport;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsSupport {
    @Test
    void RestDocsTest() throws Exception {
        mockMvc.perform(get("/restDocsTest/{id}", 1L)).andExpect(status().isOk())
                .andDo(document("test",    // 문서 식별
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
//                        requestHeaders( // 헤더 문서화
//                                headerWithName("Authorization").description("사용자 인증 및 인가를 위한 Bearer token")
//                        ),
                        pathParameters(
                                parameterWithName("id").description("테스트할 ID")
                        ),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Test API")
                                        .summary("테스트7 생성 API")
//                                        .requestFields( 요청 본문에 포함될 경우
//                                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("테스트 아이디")
//                                        )
                                        .responseFields(
                                                fieldWithPath("result").type(JsonFieldType.STRING).description("테스트 완료 화면")
                                        )
                                        .responseSchema(Schema.schema("CommentCreateResponseSchema"))
                                        .build()
                        )
                ));
    }
}
