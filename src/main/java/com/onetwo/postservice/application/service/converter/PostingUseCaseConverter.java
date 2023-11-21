package com.onetwo.postservice.application.service.converter;

import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.UpdatePostingResponseDto;
import com.onetwo.postservice.domain.Posting;

public interface PostingUseCaseConverter {
    PostPostingResponseDto postingToPostResponseDto(Posting savedPosting);

    DeletePostingResponseDto postingToDeleteResponseDto(Posting posting);

    UpdatePostingResponseDto postingToUpdateResponseDto(boolean isUpdateSuccess);

    FindPostingDetailResponseDto postingToDetailResponse(Posting posting);
}