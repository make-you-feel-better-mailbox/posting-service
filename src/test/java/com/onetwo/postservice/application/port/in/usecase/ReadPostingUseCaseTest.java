package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.service.converter.PostingUseCaseConverter;
import com.onetwo.postservice.application.service.service.PostingService;
import com.onetwo.postservice.domain.Posting;
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private final Instant filterStartDate = Instant.parse("2000-01-01T00:00:00Z");
    private final Instant filterEndDate = Instant.parse("4000-01-01T00:00:00Z");
    private final boolean mediaExist = true;

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 - 성공 테스트")
    void readDetailPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingId);

        FindPostingDetailResponseDto findPostingDetailResponseDto = new FindPostingDetailResponseDto(postingId, userId, mediaExist, postedDate);

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

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
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
        PostingFilterCommand postingFilterCommand = new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, pageRequest);

        FilteredPostingResponseDto testFilteredPosting = new FilteredPostingResponseDto(postingId, userId, content, mediaExist, Instant.now());

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        List<Posting> postingList = new ArrayList<>();
        postingList.add(posting);

        given(readPostingPort.filterPosting(any(PostingFilterCommand.class))).willReturn(postingList);
        given(postingUseCaseConverter.postingToFilteredResponse(any(Posting.class))).willReturn(testFilteredPosting);
        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPosting(postingFilterCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("[단위][Use Case] Posting Filter by user empty list 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseEmptyListSuccessTest() {
        //given
        PostingFilterCommand postingFilterCommand = new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, pageRequest);

        List<Posting> postingList = new ArrayList<>();

        given(readPostingPort.filterPosting(any(PostingFilterCommand.class))).willReturn(postingList);
        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPosting(postingFilterCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getContent().isEmpty());
    }
}