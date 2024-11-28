package com.onetwo.postservice.application.port.in.command;

import com.onetwo.postservice.application.port.SliceRequest;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.Instant;


@Getter
public final class PostingFilterCommand extends SliceRequest<PostingFilterCommand> {

    private final String userId;

    private final String content;

    private final Instant filterStartDate;

    private final Instant filterEndDate;

    public PostingFilterCommand(String userId, String content, Instant filterStartDate, Instant filterEndDate, Pageable pageable) {
        super(pageable);
        this.userId = userId;
        this.content = content;
        this.filterStartDate = filterStartDate;
        this.filterEndDate = filterEndDate;
        this.validateSelf();
    }
}
