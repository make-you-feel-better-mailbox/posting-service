package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

class UpdatePostingCommandValidationTest {

    private final Long postingId = 1L;
    private final String userId = "testUserId";
    private final String content = "content";
    private final boolean mediaExist = true;

    @Test
    @DisplayName("[단위][Command Validation] Update Posting Command Validation test - 성공 테스트")
    void updatePostingCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new UpdatePostingCommand(postingId, userId, content, mediaExist));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("[단위][Command Validation] Update Posting Command user Id Validation fail test - 실패 테스트")
    void updatePostingCommandUserIdValidationFailTest(Long testPostingId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdatePostingCommand(testPostingId, userId, content, mediaExist));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Update Posting Command posting Id Validation fail test - 실패 테스트")
    void updatePostingCommandPostingIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdatePostingCommand(postingId, testUserId, content, mediaExist));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Update Posting Command content Validation fail test - 실패 테스트")
    void updatePostingCommandContentValidationFailTest(String testContent) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UpdatePostingCommand(postingId, userId, testContent, mediaExist));
    }
}
