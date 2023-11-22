package com.onetwo.postservice.adapter.in.web.posting.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwo.postservice.adapter.in.web.config.TestConfig;
import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingFilterDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.FilterSliceRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.ReadPosingUseCase;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostingFilterController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SecurityConfig.class
                })
        })
@Import(TestConfig.class)
class PostingFilterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReadPosingUseCase readPosingUseCase;

    @MockBean
    private PostingFilterDtoMapper postingFilterDtoMapper;

    private final String userId = "testUserId";
    private final String content = "content";
    private final String contentPath = "content";
    private final PageRequest pageRequest = PageRequest.of(0, 20);
    private final String pageNumber = "pageNumber";
    private final String pageSize = "pageSize";
    private final String userIdQueryStringPath = "userId";
    private final Instant filterStartDate = Instant.parse("2000-01-01T00:00:00Z");
    private final Instant filterEndDate = Instant.parse("4000-01-01T00:00:00Z");
    private final String filterStartDatePath = "filterStartDate";
    private final String filterEndDatePath = "filterEndDate";

    @Test
    @DisplayName("[단위][Web Adapter] Posting Filter by user 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserSuccessTest() throws Exception {
        //given
        PostingFilterCommand postingFilterCommand = new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, pageRequest);

        List<FilteredPostingResponseDto> filteredPostingResponseDtoList = new ArrayList<>();

        for (int i = 1; i <= pageRequest.getPageSize(); i++) {
            FilteredPostingResponseDto testFilteredPosting = new FilteredPostingResponseDto(i, userId, Instant.now());
            filteredPostingResponseDtoList.add(testFilteredPosting);
        }

        Slice<FilteredPostingResponseDto> filteredPostingResponseDtoSlice = new SliceImpl<>(filteredPostingResponseDtoList, pageRequest, true);

        List<FilteredPostingResponse> filteredPostingResponseList = filteredPostingResponseDtoList.stream()
                .map(responseDto -> new FilteredPostingResponse(responseDto.postingId(), responseDto.userId(), responseDto.postedDate())).toList();

        Slice<FilteredPostingResponse> filteredPostingResponseSlice = new SliceImpl<>(filteredPostingResponseList, pageRequest, true);

        when(postingFilterDtoMapper.filterRequestToCommand(any(FilterSliceRequest.class))).thenReturn(postingFilterCommand);
        when(readPosingUseCase.filterPosting(any(PostingFilterCommand.class))).thenReturn(filteredPostingResponseDtoSlice);
        when(postingFilterDtoMapper.dtoToFilteredPostingResponse(any(Slice.class))).thenReturn(filteredPostingResponseSlice);

        String queryString = UriComponentsBuilder.newInstance()
                .queryParam(pageNumber, pageRequest.getPageNumber())
                .queryParam(pageSize, pageRequest.getPageSize())
                .queryParam(userIdQueryStringPath, userId)
                .queryParam(filterStartDatePath, filterStartDate)
                .queryParam(filterEndDatePath, filterEndDate)
                .queryParam(contentPath, content)
                .build()
                .encode()
                .toUriString();
        //when
        ResultActions resultActions = mockMvc.perform(
                get(GlobalUrl.POSTING_FILTER + queryString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}