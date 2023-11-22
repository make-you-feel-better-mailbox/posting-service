package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.PostingFilterByUserCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.service.converter.PostingUseCaseConverter;
import com.onetwo.postservice.application.service.service.PostingService;
import com.onetwo.postservice.common.exceptions.BadRequestException;
import com.onetwo.postservice.common.exceptions.NotFoundResourceException;
import com.onetwo.postservice.domain.Posting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReadPostingUseCaseTest {

    @InjectMocks
    private PostingService readPostingUseCase;

    @Mock
    private ReadPostingPort readPostingPort;

    @Mock
    private PostingUseCaseConverter postingUseCaseConverter;

    private final Long postingId = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final Instant postedDate = Instant.now();
    private final PageRequest pageRequest = PageRequest.of(0, 20);

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 - 성공 테스트")
    void readDetailPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingId);

        FindPostingDetailResponseDto findPostingDetailResponseDto = new FindPostingDetailResponseDto(postingId, userId, postedDate);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));
        given(postingUseCaseConverter.postingToDetailResponse(any(Posting.class))).willReturn(findPostingDetailResponseDto);
        //when
        FindPostingDetailResponseDto result = readPostingUseCase.findPostingDetail(findPostingDetailCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.userId());
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 posting does not exist - 실패 테스트")
    void readDetailPostingUseCasePostingDoesNotExistFailTest() {
        //given
        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingId);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 posting already deleted - 실패 테스트")
    void readDetailPostingUseCasePostingAlreadyDeletedFailTest() {
        //given
        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingId);

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Posting Filter by user 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseSuccessTest() {
        //given
        PostingFilterByUserCommand postingFilterByUserCommand = new PostingFilterByUserCommand(userId, pageRequest);

        FilteredPostingResponseDto testFilteredPosting = new FilteredPostingResponseDto(postingId, userId, Instant.now());

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        List<Posting> postingList = new ArrayList<>();
        postingList.add(posting);

        given(readPostingPort.findByUserId(anyString(), any(Pageable.class))).willReturn(postingList);
        given(postingUseCaseConverter.postingToFilteredResponse(any(Posting.class))).willReturn(testFilteredPosting);
        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPostingByUser(postingFilterByUserCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("[단위][Use Case] Posting Filter by user empty list 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseEmptyListSuccessTest() {
        //given
        PostingFilterByUserCommand postingFilterByUserCommand = new PostingFilterByUserCommand(userId, pageRequest);

        List<Posting> postingList = new ArrayList<>();

        given(readPostingPort.findByUserId(anyString(), any(Pageable.class))).willReturn(postingList);
        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPostingByUser(postingFilterByUserCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getContent().isEmpty());
    }
}