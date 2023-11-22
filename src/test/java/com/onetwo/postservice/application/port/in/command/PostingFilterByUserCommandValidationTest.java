package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.data.domain.PageRequest;

class PostingFilterByUserCommandValidationTest {

    private final String userId = "testUserId";
    private final PageRequest pageRequest = PageRequest.of(0, 20);

    @Test
    @DisplayName("[단위][Command Validation] Posting Filter By User Command Validation test - 성공 테스트")
    void postingFilterByUserCommandValidationSuccessTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostingFilterByUserCommand(userId, pageRequest));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command user id Validation fail test - 실패 테스트")
    void postingFilterByUserCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new PostingFilterByUserCommand(testUserId, pageRequest));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command pageable Validation fail test - 실패 테스트")
    void postingFilterByUserCommandPageableValidationFailTest(PageRequest testPageRequest) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new PostingFilterByUserCommand(userId, testPageRequest));
    }
}