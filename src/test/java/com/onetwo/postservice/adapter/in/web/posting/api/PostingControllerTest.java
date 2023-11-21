package com.onetwo.postservice.adapter.in.web.posting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.postservice.adapter.in.web.config.TestConfig;
import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.DeletePostingResponse;
import com.onetwo.postservice.adapter.in.web.posting.response.PostPostingResponse;
import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.DeletePostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.common.GlobalUrl;
import com.onetwo.postservice.common.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostingController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
@Import(TestConfig.class)
class PostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostPostingUseCase postPostingUseCase;

    @MockBean
    private DeletePostingUseCase deletePostingUseCase;

    @MockBean
    private PostingDtoMapper postingDtoMapper;

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 등록 - 성공 테스트")
    void postPostingSuccessTest() throws Exception {
        //given
        PostPostingRequest postPostingRequest = new PostPostingRequest(content);
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        PostPostingResponseDto postPostingResponseDto = new PostPostingResponseDto(postingIdx, true);
        PostPostingResponse postPostingResponse = new PostPostingResponse(postingIdx, true);

        when(postingDtoMapper.postRequestToCommand(anyString(), any(PostPostingRequest.class))).thenReturn(postPostingCommand);
        when(postPostingUseCase.postPosting(any(PostPostingCommand.class))).thenReturn(postPostingResponseDto);
        when(postingDtoMapper.dtoToPostResponse(any(PostPostingResponseDto.class))).thenReturn(postPostingResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post(GlobalUrl.POST_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postPostingRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 삭제 - 성공 테스트")
    void deletePostingSuccessTest() throws Exception {
        //given
        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(postingIdx, userId);
        DeletePostingResponseDto deletePostingResponseDto = new DeletePostingResponseDto(true);
        DeletePostingResponse deletePostingResponse = new DeletePostingResponse(true);

        when(postingDtoMapper.deleteRequestToCommand(anyString(), anyLong())).thenReturn(deletePostingCommand);
        when(deletePostingUseCase.deletePosting(any(DeletePostingCommand.class))).thenReturn(deletePostingResponseDto);
        when(postingDtoMapper.dtoToDeleteResponse(any(DeletePostingResponseDto.class))).thenReturn(deletePostingResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.POST_ROOT + "/{posting-id}", postingIdx)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}