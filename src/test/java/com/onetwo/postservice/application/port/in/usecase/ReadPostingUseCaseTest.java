package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
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

import java.time.Instant;
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

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final Instant postedDate = Instant.now();

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 - 성공 테스트")
    void readDetailPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingIdx);

        FindPostingDetailResponseDto findPostingDetailResponseDto = new FindPostingDetailResponseDto(postingIdx, userId, postedDate);

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
        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingIdx);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 posting already readd - 실패 테스트")
    void readDetailPostingUseCasePostingAlreadyreaddFailTest() {
        //given
        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingIdx);

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }
}