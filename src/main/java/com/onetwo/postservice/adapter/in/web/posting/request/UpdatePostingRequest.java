package com.onetwo.postservice.adapter.in.web.posting.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdatePostingRequest(@NotEmpty String content) {
}
