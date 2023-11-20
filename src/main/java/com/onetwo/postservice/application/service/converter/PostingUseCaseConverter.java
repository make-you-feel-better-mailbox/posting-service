package com.onetwo.postservice.application.service.converter;

import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.domain.Posting;

public interface PostingUseCaseConverter {
    PostPostingResponseDto postingToPostResponseDto(Posting savedPosting);
}
