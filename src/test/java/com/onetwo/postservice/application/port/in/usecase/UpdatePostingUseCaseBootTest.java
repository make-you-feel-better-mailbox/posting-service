package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.UpdatePostingCommand;
import com.onetwo.postservice.application.port.in.response.UpdatePostingResponseDto;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.domain.Posting;
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UpdatePostingUseCaseBootTest {

    @Autowired
    private UpdatePostingUseCase updatePostingUseCase;

    @Autowired
    private RegisterPostingPort registerPostingPort;

    private final Long postingId = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final boolean mediaExist = true;

    @Test
    @DisplayName("[통합][Use Case] Posting 수정 - 성공 테스트")
    void updatePostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        Posting savedPosting = registerPostingPort.registerPosting(posting);

        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(savedPosting.getId(), userId, content, mediaExist);

        //when
        UpdatePostingResponseDto result = updatePostingUseCase.updatePosting(updatePostingCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isUpdateSuccess());
    }

    @Test
    @DisplayName("[통합][Use Case] Posting 수정 posting does not exist - 실패 테스트")
    void updatePostingUseCasePostingDoesNotExistFailTest() {
        //given
        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(postingId, userId, content, mediaExist);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> updatePostingUseCase.updatePosting(updatePostingCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] Posting 수정 posting already deleted - 실패 테스트")
    void updatePostingUseCasePostingAlreadyDeletedFailTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        posting.deletePosting();

        Posting savedPosting = registerPostingPort.registerPosting(posting);

        UpdatePostingCommand updatePostingCommand = new UpdatePostingCommand(savedPosting.getId(), userId, content, mediaExist);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> updatePostingUseCase.updatePosting(updatePostingCommand));
    }
}