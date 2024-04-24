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
    public FindPostingDetailResponseDto postingToDetailResponse(Posting posting, String userNickname) {
        return new FindPostingDetailResponseDto(posting.getId(), posting.getUserId(), userNickname, posting.getContent(), posting.isMediaExist(), posting.getCreatedAt());
    }

    @Override
    public FilteredPostingResponseDto postingToFilteredResponse(Posting posting, String userNickname) {
        return new FilteredPostingResponseDto(posting.getId(), posting.getUserId(), userNickname, posting.getContent(), posting.isMediaExist(), posting.getCreatedAt());
    }
}
