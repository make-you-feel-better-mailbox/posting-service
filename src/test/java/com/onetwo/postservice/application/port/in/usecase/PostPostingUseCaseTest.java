package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.application.service.converter.PostingUseCaseConverter;
import com.onetwo.postservice.application.service.service.PostingService;
import com.onetwo.postservice.domain.Posting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostPostingUseCaseTest {

    @InjectMocks
    private PostingService postPostingUseCase;

    @Mock
    private RegisterPostingPort registerPostingPort;

    @Mock
    private PostingUseCaseConverter postingUseCaseConverter;

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";

    @Test
    @DisplayName("[단위][Use Case] Posting 등록 - 성공 테스트")
    void postPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);

        PostPostingResponseDto postPostingResponseDto = new PostPostingResponseDto(postingIdx, true);

        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        given(registerPostingPort.registerPosting(any(Posting.class))).willReturn(posting);
        given(postingUseCaseConverter.postingToPostResponseDto(any(Posting.class))).willReturn(postPostingResponseDto);
        //when
        PostPostingResponseDto result = postPostingUseCase.postPosting(postPostingCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPostSuccess());
    }
}