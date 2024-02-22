package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.domain.Posting;
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
    private final Instant filterStartDate = Instant.parse("2000-01-01T00:00:00Z");
    private final Instant filterEndDate = Instant.parse("4000-01-01T00:00:00Z");
    private final boolean mediaExist = true;


    @Test
    @DisplayName("[통합][Use Case] Posting 상세 조회 - 성공 테스트")
    void readDetailPostingUseCaseSuccessTest() {
        //given
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
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
        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);
        posting.deletePosting();

        Posting savedPosting = registerPostingPort.registerPosting(posting);

        FindPostingDetailCommand findPostingDetailCommand = new FindPostingDetailCommand(savedPosting.getId());

        //when then
        Assertions.assertThrows(BadRequestException.class, () -> readPostingUseCase.findPostingDetail(findPostingDetailCommand));
    }

    @Test
    @DisplayName("[통합][Use Case] Posting Filter 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseSuccessTest() {
        //given
        PostingFilterCommand postingFilterCommand = new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, pageRequest);

        PostPostingCommand postPostingCommand = new PostPostingCommand(userId, content, mediaExist);
        Posting posting = Posting.createNewPostingByCommand(postPostingCommand);

        registerPostingPort.registerPosting(posting);

        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPosting(postingFilterCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("[통합][Use Case] Posting Filter by user empty list 조회 성공 - 성공 테스트")
    void getFilteredPostingByUserUseCaseEmptyListSuccessTest() {
        //given
        PostingFilterCommand postingFilterCommand = new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, pageRequest);

        //when
        Slice<FilteredPostingResponseDto> result = readPostingUseCase.filterPosting(postingFilterCommand);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getContent().isEmpty());
    }
}