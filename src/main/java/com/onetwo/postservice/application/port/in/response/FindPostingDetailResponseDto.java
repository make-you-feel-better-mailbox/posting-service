package com.onetwo.postservice.application.port.in.response;

import java.time.Instant;

public record FindPostingDetailResponseDto(long postingId, String userId, String content, boolean mediaExist,
                                           Instant postedDate) {
}
