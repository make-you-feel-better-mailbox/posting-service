package com.onetwo.postservice.application.service.converter;

import com.onetwo.postservice.application.port.in.response.*;
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

    @Override
    public UpdatePostingResponseDto postingToUpdateResponseDto(boolean isUpdateSuccess) {
        return new UpdatePostingResponseDto(isUpdateSuccess);
    }

    @Override
    public FindPostingDetailResponseDto postingToDetailResponse(Posting posting) {
        return new FindPostingDetailResponseDto(posting.getId(), posting.getUserId(), posting.getCreatedAt());
    }

    @Override
    public FilteredPostingResponseDto postingToFilteredResponse(Posting posting) {
        return new FilteredPostingResponseDto(posting.getId(), posting.getUserId(), posting.getContent(), posting.getCreatedAt());
    }
}
