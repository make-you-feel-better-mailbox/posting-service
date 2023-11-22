package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.common.exceptions.BadRequestException;
import com.onetwo.postservice.common.exceptions.NotFoundResourceException;
import com.onetwo.postservice.domain.Posting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReadPostingUseCaseBootTest {

    @Autowired
    private ReadPosingUseCase readPostingUseCase;

    @Autowired
    private RegisterPostingPort registerPostingPort;

    private final Long postingIdx = 1L;
    private final String userId = "testUserId";
    private final String content = "content";

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 - 성공 테스트")
    void readDetailPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        Posting savedPosting = registerPostingPort.registerPosting(posting);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(savedPosting.getId());

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

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[단위][Use Case] Posting 상세 조회 posting already readd - 실패 테스트")
    void readDetailPostingUseCasePostingAlreadyreaddFailTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        Posting savedPosting = registerPostingPort.registerPosting(posting);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(savedPosting.getId());

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }
}