package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostPostingUseCaseBootTest {

    @Autowired
    private PostPostingUseCase postPostingUseCase;

    private final String userId = "testUserId";
    private final String content = "content";
    private final boolean mediaExist = true;

    @Test
    @DisplayName("[통합][Use Case] Posting 등록 - 성공 테스트")
    void postPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);

        //when
        PostPostingResponseDto result = postPostingUseCase.postPosting(postPostingCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.postingId());
        Assertions.assertTrue(result.isPostSuccess());
    }
}