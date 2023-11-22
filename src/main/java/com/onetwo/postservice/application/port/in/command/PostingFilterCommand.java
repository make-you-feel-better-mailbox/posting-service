package com.onetwo.postservice.application.port.in.command;

import com.onetwo.postservice.application.port.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.Instant;


@Getter
public final class PostingFilterCommand extends SelfValidating<PostingFilterCommand> {

    private final String userId;

    private final String content;

    private final Instant filterStartDate;

    private final Instant filterEndDate;

    @NotNull
    private final Pageable pageable;

    public PostingFilterCommand(String userId, String content, Instant filterStartDate, Instant filterEndDate, Pageable pageable) {
        this.userId = userId;
        this.content = content;
        this.filterStartDate = filterStartDate;
        this.filterEndDate = filterEndDate;
        this.pageable = pageable;
        this.validateSelf();
    }
}
