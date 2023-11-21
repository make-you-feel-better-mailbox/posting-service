package com.onetwo.postservice.adapter.in.web.posting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.postservice.adapter.in.web.config.TestHeader;
import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.request.UpdatePostingRequest;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.common.GlobalStatus;
import com.onetwo.postservice.common.GlobalUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestHeader.class)
class PostingControllerBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostPostingUseCase postPostingUseCase;

    @Autowired
    private TestHeader testHeader;

    private final String userId = "testUserId";
    private final String content = "content";

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting 등록 - 성공 테스트")
    void postPostingSuccessTest() throws Exception {
        //given
        PostPostingRequest postPostingRequest = new PostPostingRequest(content);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.POST_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postPostingRequest))
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post-posting",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("등록할 posting 본문")
                                ),
                                responseFields(
                                        fieldWithPath("postingId").type(JsonFieldType.NUMBER).description("등록 성공시 posting id"),
                                        fieldWithPath("isPostSuccess").type(JsonFieldType.BOOLEAN).description("등록 완료 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting 삭제 - 성공 테스트")
    void deletePostingSuccessTest() throws Exception {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        PostPostingResponseDto postPostingResponseDto = postPostingUseCase.postPosting(postPostingCommand);

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.POST_ROOT + "/{posting-id}", postPostingResponseDto.postingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-posting",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                pathParameters(
                                        parameterWithName("posting-id").description("삭제할 posting id")
                                ),
                                responseFields(
                                        fieldWithPath("isDeleteSuccess").type(JsonFieldType.BOOLEAN).description("삭제 성공 여부")
                                )
                        )
                );
    }

    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting 수정 - 성공 테스트")
    void updatePostingSuccessTest() throws Exception {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        PostPostingResponseDto postPostingResponseDto = postPostingUseCase.postPosting(postPostingCommand);

        UpdatePostingRequest updatePostingRequest = new UpdatePostingRequest(content);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.POST_ROOT + "/{posting-id}", postPostingResponseDto.postingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .content(objectMapper.writeValueAsString(updatePostingRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-posting",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                pathParameters(
                                        parameterWithName("posting-id").description("수정할 posting id")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 posting 본문")
                                ),
                                responseFields(
                                        fieldWithPath("isUpdateSuccess").type(JsonFieldType.BOOLEAN).description("수정 성공 여부")
                                )
                        )
                );
    }


    @Test
    @Transactional
    @DisplayName("[통합][Web Adapter] Posting 상세 조회 - 성공 테스트")
    void getDetailPostingSuccessTest() throws Exception {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        PostPostingResponseDto postPostingResponseDto = postPostingUseCase.postPosting(postPostingCommand);

        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POST_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postPostingResponseDto.postingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(testHeader.getRequestHeaderWithMockAccessKey(userId))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-detail-posting",
                                requestHeaders(
                                        headerWithName(GlobalStatus.ACCESS_ID).description("서버 Access id"),
                                        headerWithName(GlobalStatus.ACCESS_KEY).description("서버 Access key"),
                                        headerWithName(GlobalStatus.ACCESS_TOKEN).description("유저의 access-token")
                                ),
                                pathParameters(
                                        parameterWithName("posting-id").description("조회할 posting id")
                                ),
                                responseFields(
                                        fieldWithPath("postingId").type(JsonFieldType.NUMBER).description("포스팅 id"),
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("등록 유저"),
                                        fieldWithPath("postedDate").type(JsonFieldType.STRING).description("등록일자")
                                )
                        )
                );
    }
}