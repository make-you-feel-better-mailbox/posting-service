package com.onetwo.postservice.application.port.in.command;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import onetwo.mailboxcommonconfig.common.SelfValidating;

@Getter
public final class FindPostingDetailCommand extends SelfValidating<FindPostingDetailCommand> {

    @NotNull
    private final Long postingId;

    public FindPostingDetailCommand(Long postingId) {
        this.postingId = postingId;
        this.validateSelf();
    }
}
