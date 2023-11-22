package com.onetwo.postservice.application.port.in.command;

import com.onetwo.postservice.application.port.SelfValidating;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.domain.Pageable;


@Getter
public final class PostingFilterByUserCommand extends SelfValidating<PostingFilterByUserCommand> {

    @NotEmpty
    private final String userId;

    @NotNull
    private Pageable pageable;

    public PostingFilterByUserCommand(String userId, Pageable pageable) {
        this.userId = userId;
        this.pageable = pageable;
        this.validateSelf();
    }
}
