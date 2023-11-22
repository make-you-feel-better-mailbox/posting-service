package com.onetwo.postservice.application.port.in.command;

import com.onetwo.postservice.application.port.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public final class UpdatePostingCommand extends SelfValidating<UpdatePostingCommand> {

    @NotNull
    private final Long postingId;

    @NotEmpty
    private final String userId;

    @NotEmpty
    private final String content;

    public UpdatePostingCommand(Long postingId, String userId, String content) {
        this.postingId = postingId;
        this.userId = userId;
        this.content = content;
        this.validateSelf();
    }
}
