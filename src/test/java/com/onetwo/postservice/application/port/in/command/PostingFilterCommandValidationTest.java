package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;

class PostingFilterCommandValidationTest {

    private final String userId = "testUserId";
    private final String content = "content";
    private final Instant filterStartDate = Instant.parse("2000-01-01T00:00:00Z");
    private final Instant filterEndDate = Instant.parse("4000-01-01T00:00:00Z");
    private final PageRequest pageRequest = PageRequest.of(0, 20);

    @Test
    @DisplayName("[단위][Command Validation] Posting Filter By User Command Validation test - 성공 테스트")
    void postingFilterByUserCommandValidationSuccessTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, pageRequest));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command user id Validation success test - 성공 테스트")
    void postingFilterByUserCommandUserIdValidationSuccessTest(String testUserId) {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostingFilterCommand(testUserId, content, filterStartDate, filterEndDate, pageRequest));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command content Validation success test - 성공 테스트")
    void postingFilterByUserCommandContentValidationSuccessTest(String testContent) {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostingFilterCommand(userId, testContent, filterStartDate, filterEndDate, pageRequest));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command filter start date Validation success test - 성공 테스트")
    void postingFilterByUserCommandFilterStartDateValidationSuccessTest(Instant testFilterStartDate) {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostingFilterCommand(userId, content, testFilterStartDate, filterEndDate, pageRequest));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command filter end date Validation success test - 성공 테스트")
    void postingFilterByUserCommandFilterEndDateValidationSuccessTest(Instant testFilterEndDate) {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostingFilterCommand(userId, content, filterStartDate, testFilterEndDate, pageRequest));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command pageable Validation fail test - 실패 테스트")
    void postingFilterByUserCommandPageableValidationFailTest(PageRequest testPageRequest) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new PostingFilterCommand(userId, content, filterStartDate, filterEndDate, testPageRequest));
    }
}