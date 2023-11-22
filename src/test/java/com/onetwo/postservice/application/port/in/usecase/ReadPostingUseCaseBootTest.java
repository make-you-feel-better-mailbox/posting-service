package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.PostingFilterByUserCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReadPostingUseCaseBootTest {

    @Autowired
    private ReadPosingUseCase readPostingUseCase;

    @Autowired
    private RegisterPostingPort registerPostingPort;

    private final Long postingId = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final PageRequest pageRequest = PageRequest.of(0, 20);

    @Test
    @DisplayName("[통합][Use Case] Posting 상세 조회 - 성공 테스트")
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
    @DisplayName("[통합][Use Case] Posting 상세 조회 posting does not exist - 실패 테스트")
    void readDetailPostingUseCasePostingDoesNotExistFailTest() {
        //given
        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(postingId);

        //when then
        Assertions.assertThrows(NotFoundResourceException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] Posting 상세 조회 posting already deleted - 실패 테스트")
    void readDetailPostingUseCasePostingAlreadyDeletedFailTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        Posting savedPosting = registerPostingPort.registerPosting(posting);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(savedPosting.getId());

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] Posting Filter by user 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseSuccessTest() {
        //given
        PostingFilterByUserCommand postingFilterByUserCommand = new PostingFilterByUserCommand(userId, pageRequest);

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        registerPostingPort.registerPosting(posting);

        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPostingByUser(postingFilterByUserCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("[통합][Use Case] Posting Filter by user empty list 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseEmptyListSuccessTest() {
        //given
        PostingFilterByUserCommand postingFilterByUserCommand = new PostingFilterByUserCommand(userId, pageRequest);

        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPostingByUser(postingFilterByUserCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getContent().isEmpty());
    }
}