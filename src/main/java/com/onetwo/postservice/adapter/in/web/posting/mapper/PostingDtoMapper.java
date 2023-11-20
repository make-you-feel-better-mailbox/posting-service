package com.onetwo.postservice.adapter.in.web.posting.mapper;

import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.PostPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;

public interface PostingDtoMapper {
    PostPostingCommand postRequestToCommand(String userId, PostPostingRequest postPostingRequest);

    PostPostingResponse dtoToPostResponse(PostPostingResponseDto postPostingResponseDto);
}
