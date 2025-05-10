package timetogeter.global;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;

@TestConfiguration
public class RestDocsConfig {
    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}", // identifier
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                resource(
                        ResourceSnippetParameters.builder()
                                .tag("Auth")
                                .description("테스트 API입니다.")
                                .responseFields(
                                        fieldWithPath("text").description("응답 메시지")
                                )
                                .build()
                )
        );
    }
}
