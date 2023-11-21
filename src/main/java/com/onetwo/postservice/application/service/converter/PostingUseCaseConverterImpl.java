package com.onetwo.postservice.application.service.converter;

import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.domain.Posting;
import org.springframework.stereotype.Component;

@Component
public class PostingUseCaseConverterImpl implements PostingUseCaseConverter {
    @Override
    public PostPostingResponseDto postingToPostResponseDto(Posting savedPosting) {
        return new PostPostingResponseDto(savedPosting.getId(), savedPosting.isNotDeleted());
    }

    @Override
    public DeletePostingResponseDto postingToDeleteResponseDto(Posting posting) {
        return new DeletePostingResponseDto(posting.isDeleted());
    }
}
