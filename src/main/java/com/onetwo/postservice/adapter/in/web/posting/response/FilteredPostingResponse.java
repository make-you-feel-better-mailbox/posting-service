package com.onetwo.postservice.adapter.in.web.posting.response;

import java.time.Instant;

public record FilteredPostingResponse(long postingId, String userId, String content, boolean mediaExist,
                                      Instant postedDate) {
}
