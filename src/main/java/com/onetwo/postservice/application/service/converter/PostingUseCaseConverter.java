package com.onetwo.postservice.application.service.converter;

import com.onetwo.postservice.application.port.in.response.*;
import com.onetwo.postservice.domain.Posting;

public interface PostingUseCaseConverter {
    PostPostingResponseDto postingToPostResponseDto(Posting savedPosting);

    DeletePostingResponseDto postingToDeleteResponseDto(Posting posting);

    UpdatePostingResponseDto postingToUpdateResponseDto(boolean isUpdateSuccess);

    FindPostingDetailResponseDto postingToDetailResponse(Posting posting);

    FilteredPostingResponseDto postingToFilteredResponse(Posting posting);
}
