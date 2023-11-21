package com.onetwo.postservice.adapter.in.web.posting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.postservice.adapter.in.web.config.TestConfig;
import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.request.UpdatePostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.DeletePostingResponse;
import com.onetwo.postservice.adapter.in.web.posting.response.PostPostingResponse;
import com.onetwo.postservice.adapter.in.web.posting.response.PostingDetailResponse;
import com.onetwo.postservice.adapter.in.web.posting.response.UpdatePostingResponse;
import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.UpdatePostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.UpdatePostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.DeletePostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.ReadPosingUseCase;
import com.onetwo.postservice.application.port.in.usecase.UpdatePostingUseCase;
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

import java.time.Instant;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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
    private UpdatePostingUseCase updatePostingUseCase;

    @MockBean
    private ReadPosingUseCase readPosingUseCase;

    @MockBean
    private PostingDtoMapper postingDtoMapper;

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final Instant postedDate = Instant.now();

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

        when(postingDtoMapper.deleteRequestToCommand(anyLong(), anyString())).thenReturn(deletePostingCommand);
        when(deletePostingUseCase.deletePosting(any(DeletePostingCommand.class))).thenReturn(deletePostingResponseDto);
        when(postingDtoMapper.dtoToDeleteResponse(any(DeletePostingResponseDto.class))).thenReturn(deletePostingResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete(GlobalUrl.POST_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingIdx)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 수정 - 성공 테스트")
    void updatePostingSuccessTest() throws Exception {
        //given
        UpdatePostingRequest updatePostingRequest = new UpdatePostingRequest(content);
        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(postingIdx, userId, content);
        UpdatePostingResponseDto updatePostingResponseDto = new UpdatePostingResponseDto(true);
        UpdatePostingResponse updatePostingResponse = new UpdatePostingResponse(true);

        when(postingDtoMapper.updateRequestToCommand(anyLong(), anyString(), any(UpdatePostingRequest.class))).thenReturn(updatePostingCommand);
        when(updatePostingUseCase.updatePosting(any(UpdatePostingCommand.class))).thenReturn(updatePostingResponseDto);
        when(postingDtoMapper.dtoToUpdateResponse(any(UpdatePostingResponseDto.class))).thenReturn(updatePostingResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                put(GlobalUrl.POST_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingIdx)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePostingRequest))
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = userId)
    @DisplayName("[단위][Web Adapter] Posting 상세 조회 - 성공 테스트")
    void getDetailPostingSuccessTest() throws Exception {
        //given
        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingIdx);
        FindPostingDetailResponseDto findPostingDetailResponseDto = new FindPostingDetailResponseDto(postingIdx, userId, postedDate);
        PostingDetailResponse postingDetailResponse = new PostingDetailResponse(postingIdx, userId, postedDate);

        when(postingDtoMapper.findRequestToCommand(anyLong())).thenReturn(findPostingDetailCommand);
        when(readPosingUseCase.findPostingDetail(any(FindPostingDetailCommand.class))).thenReturn(findPostingDetailResponseDto);
        when(postingDtoMapper.dtoToDetailResponse(any(FindPostingDetailResponseDto.class))).thenReturn(postingDetailResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POST_ROOT + GlobalUrl.PATH_VARIABLE_POSTING_ID_WITH_BRACE, postingIdx)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}