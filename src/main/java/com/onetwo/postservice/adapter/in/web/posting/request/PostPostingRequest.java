package com.onetwo.postservice.adapter.in.web.posting.request;

import jakarta.validation.constraints.NotEmpty;

public record PostPostingRequest(@NotEmpty String content,
                                 Boolean mediaExist) {
}
