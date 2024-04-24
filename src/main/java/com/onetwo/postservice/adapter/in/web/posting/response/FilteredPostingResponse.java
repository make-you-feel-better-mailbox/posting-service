package com.onetwo.postservice.adapter.in.web.posting.response;

import java.time.Instant;

public record FilteredPostingResponse(long postingId, String userId, String userNickname, String content,
                                      boolean mediaExist,
                                      Instant postedDate) {
}
