package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import onetwo.mailboxcommonconfig.common.SelfValidating;

@Getter
public final class DeletePostingCommand extends SelfValidating<DeletePostingCommand> {

    @NotNull
    private final Long postingId;
    @NotEmpty
    private final String userId;

    public DeletePostingCommand(Long postingId, String userId) {
        this.postingId = postingId;
        this.userId = userId;
        this.validateSelf();
    }
}
