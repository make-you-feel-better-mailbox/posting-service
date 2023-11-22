package com.onetwo.postservice.adapter.in.web.posting.response;

import java.time.Instant;

public record PostingDetailResponse(long postingId, String userId, Instant postedDate) {
}
