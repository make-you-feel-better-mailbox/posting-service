package com.onetwo.postservice.adapter.in.web.posting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.postservice.adapter.in.web.config.TestConfig;
import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.request.UpdatePostingRequest;
import com.onetwo.postservice.application.port.in.usecase.DeletePostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.ReadPosingUseCase;
import com.onetwo.postservice.application.port.in.usecase.UpdatePostingUseCase;
import com.onetwo.postservice.common.GlobalUrl;
import com.onetwo.postservice.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostingController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
@Import(TestConfig.class)
class PostingControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostPostingUseCase postPostingUseCase;

    @MockBean
    private DeletePostingUseCase deletePostingUseCase;

    @MockBean
    private UpdatePostingUseCase updatePostingUseCase;

    @MockBean
    private ReadPosingUseCase readPosingUseCase;

    @MockBean
    private PostingDtoMapper postingDtoMapper;

    private final Long postingId = 1L;
    private final String userId = "testUserId";
    private final String content = "content";

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 등록 content validation fail - 실패 테스트")
    void postPostingContentValidationFailTest(String testContent) throws Exception {
        //given
        PostPostingRequest postPostingRequest = new PostPostingRequest(testContent);

        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.POSTING_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postPostingRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 삭제 posting id validation fail - 실패 테스트")
    void deletePostingContentValidationFailTest() throws Exception {
        //given
        String postingId = "badPostingId";

        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.POSTING_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 수정 posting id validation fail - 실패 테스트")
    void updatePostingContentValidationFailTest() throws Exception {
        //given
        String postingId = "badPostingId";

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.POSTING_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 수정 content validation fail - 실패 테스트")
    void updatePostingContentValidationFailTest(String testContent) throws Exception {
        //given
        UpdatePostingRequest updatePostingRequest = new UpdatePostingRequest(testContent);

        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.POSTING_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePostingRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 상세조회 posting id validation fail - 실패 테스트")
    void getDetailPostingContentValidationFailTest() throws Exception {
        //given
        String postingId = "badPostingId";

        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POSTING_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }
}