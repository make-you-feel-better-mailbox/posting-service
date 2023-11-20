package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PostPostingCommandValidationTest {

    private final String userId = "testUserId";
    private final String content = "content";

    @Test
    @DisplayName("[단위][Command Validation] Post Posting Command Validation test - 성공 테스트")
    void postPostingCommandValidationTest() {
        //given when then
        Assertions.assertDoesNotThrow(() -> new PostPostingCommand(userId, content));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Post Posting Command user Id Validation fail test - 실패 테스트")
    void postPostingCommandUserIdValidationFailTest(String testUserId) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new PostPostingCommand(testUserId, content));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("[단위][Command Validation] Post Posting Command content Validation fail test - 실패 테스트")
    void postPostingCommandContentValidationFailTest(String testContent) {
        //given when then
        Assertions.assertThrows(ConstraintViolationException.class, () -> new PostPostingCommand(userId, testContent));
    }
}