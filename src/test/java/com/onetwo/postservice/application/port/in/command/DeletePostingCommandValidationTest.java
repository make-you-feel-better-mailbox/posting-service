package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

class DeletePostingCommandValidationTest {

    private final Long postingId = 1L;
    private final String userId = "testUserId";

    @Test
    @DisplayName("[단위][Command Validation] Delete Posting Command Validation test - 성공 테스트")
    void deletePostingCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new DeletePostingCommand(postingId, userId));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Delete Posting Command user Id Validation fail test - 실패 테스트")
    void deletePostingCommandUserIdValidationFailTest(Long testPostingId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeletePostingCommand(testPostingId, userId));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Delete Posting Command posting Id Validation fail test - 실패 테스트")
    void deletePostingCommandPostingIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new DeletePostingCommand(postingId, testUserId));
    }
}