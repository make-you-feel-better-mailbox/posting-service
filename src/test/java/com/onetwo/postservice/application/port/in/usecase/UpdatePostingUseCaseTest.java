package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.UpdatePostingCommand;
import com.onetwo.postservice.application.port.in.response.UpdatePostingResponseDto;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.port.out.UpdatePostingPort;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdatePostingUseCaseTest {

    @InjectMocks
    private PostingService updatePostingUseCase;

    @Mock
    private ReadPostingPort readPostingPort;

    @Mock
    private UpdatePostingPort updatePostingPort;

    @Mock
    private PostingUseCaseConverter postingUseCaseConverter;

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final boolean mediaExist = true;

    @Test
    @DisplayName("[단위][Use Case] Posting 수정 - 성공 테스트")
    void updatePostingUseCaseSuccessTest() {
        //given
        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(postingIdx, userId, content, mediaExist);

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        UpdatePostingResponseDto updatePostingResponseDto = new UpdatePostingResponseDto(true);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));
        given(postingUseCaseConverter.postingToUpdateResponseDto(anyBoolean())).willReturn(updatePostingResponseDto);
        //when
        UpdatePostingResponseDto result = updatePostingUseCase.updatePosting(updatePostingCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isUpdateSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 수정 posting does not exist - 실패 테스트")
    void updatePostingUseCasePostingDoesNotExistFailTest() {
        //given
        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(postingIdx, userId, content, mediaExist);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> updatePostingUseCase.updatePosting(updatePostingCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 수정 posting already deleted - 실패 테스트")
    void updatePostingUseCasePostingAlreadyDeletedFailTest() {
        //given
        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(postingIdx, userId, content, mediaExist);
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updatePostingUseCase.updatePosting(updatePostingCommand));
    }
}