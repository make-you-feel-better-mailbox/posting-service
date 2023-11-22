package com.onetwo.postservice.application.port.in.command;

import com.onetwo.postservice.application.port.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public final class PostPostingCommand extends SelfValidating<PostPostingCommand> {

    @NotEmpty
    private final String userId;

    @NotEmpty
    private final String content;

    public PostPostingCommand(String userId, String content) {
        this.userId = userId;
        this.content = content;
        this.validateSelf();
    }
}
