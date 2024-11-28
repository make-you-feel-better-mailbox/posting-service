package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class FindPostingDetailCommandTest {

    private final Long postingId = 1L;

    @Test
    @DisplayName("[단위][Command Validation] Find Posting Detail Command Validation test - 성공 테스트")
    void findPostingDetailCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new FindPostingDetailCommand(postingId));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Find Posting Detail Command posting Id Validation fail test - 실패 테스트")
    void findPostingDetailCommandPostingIdValidationFailTest(Long testPostingId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new FindPostingDetailCommand(testPostingId));
    }
}