package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.port.out.UpdatePostingPort;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeletePostingUseCaseTest {

    @InjectMocks
    private PostingService deletePostingUseCase;

    @Mock
    private ReadPostingPort readPostingPort;

    @Mock
    private UpdatePostingPort updatePostingPort;

    @Mock
    private PostingUseCaseConverter postingUseCaseConverter;

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";

    @Test
    @DisplayName("[단위][Use Case] Posting 삭제 - 성공 테스트")
    void deletePostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(postingIdx, userId);

        DeletePostingResponseDto deletePostingResponseDto = new DeletePostingResponseDto(true);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));
        given(postingUseCaseConverter.postingToDeleteResponseDto(any(Posting.class))).willReturn(deletePostingResponseDto);
        //when
        DeletePostingResponseDto result = deletePostingUseCase.deletePosting(deletePostingCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isDeleteSuccess());
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 삭제 posting does not exist - 실패 테스트")
    void deletePostingUseCasePostingDoesNotExistFailTest() {
        //given
        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(postingIdx, userId);

        given(readPostingPort.findById(anyLong())).willReturn(Optional.empty());

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deletePostingUseCase.deletePosting(deletePostingCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 삭제 posting already deleted - 실패 테스트")
    void deletePostingUseCasePostingAlreadyDeletedFailTest() {
        //given
        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(postingIdx, userId);
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        given(readPostingPort.findById(anyLong())).willReturn(Optional.of(posting));

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> deletePostingUseCase.deletePosting(deletePostingCommand));
    }
}