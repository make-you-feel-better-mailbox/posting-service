package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
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
class DeletePostingUseCaseBootTest {

    @Autowired
    private DeletePostingUseCase deletePostingUseCase;

    @Autowired
    private RegisterPostingPort registerPostingPort;

    private final Long postingId = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final boolean mediaExist = true;

    @Test
    @DisplayName("[통합][Use Case] Posting 삭제 - 성공 테스트")
    void deletePostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        Posting savedPosting = registerPostingPort.registerPosting(posting);

        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(savedPosting.getId(), userId);

        //when
        DeletePostingResponseDto result = deletePostingUseCase.deletePosting(deletePostingCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isDeleteSuccess());
    }

    @Test
    @DisplayName("[통합][Use Case] Posting 삭제 posting does not exist - 실패 테스트")
    void deletePostingUseCasePostingDoesNotExistFailTest() {
        //given
        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(postingId, userId);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> deletePostingUseCase.deletePosting(deletePostingCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] Posting 삭제 posting already deleted - 실패 테스트")
    void deletePostingUseCasePostingAlreadyDeletedFailTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        posting.deletePosting();

        Posting savedPosting = registerPostingPort.registerPosting(posting);

        DeletePostingCommand deletePostingCommand = new DeletePostingCommand(savedPosting.getId(), userId);

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> deletePostingUseCase.deletePosting(deletePostingCommand));
    }
}