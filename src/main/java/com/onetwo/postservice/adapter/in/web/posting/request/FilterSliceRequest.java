package com.onetwo.postservice.adapter.in.web.posting.request;

import java.time.Instant;

public record FilterSliceRequest(String userId,
                                 String content,
                                 Instant filterStartDate,
                                 Instant filterEndDate,
                                 Integer pageNumber,
                                 Integer pageSize,
                                 String sort) {
}
