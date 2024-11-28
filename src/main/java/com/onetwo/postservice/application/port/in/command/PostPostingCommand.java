package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import onetwo.mailboxcommonconfig.common.SelfValidating;

@Getter
public final class PostPostingCommand extends SelfValidating<PostPostingCommand> {

    @NotEmpty
    private final String userId;

    @NotEmpty
    private final String content;

    private final boolean mediaExist;

    public PostPostingCommand(String userId, String content, boolean mediaExist) {
        this.userId = userId;
        this.content = content;
        this.mediaExist = mediaExist;
        this.validateSelf();
    }
}
