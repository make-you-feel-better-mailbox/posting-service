package com.onetwo.postservice.adapter.in.web.posting.mapper;

import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.request.UpdatePostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.DeletePostingResponse;
import com.onetwo.postservice.adapter.in.web.posting.response.PostPostingResponse;
import com.onetwo.postservice.adapter.in.web.posting.response.UpdatePostingResponse;
import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.command.UpdatePostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.UpdatePostingResponseDto;

public interface PostingDtoMapper {
    PostPostingCommand postRequestToCommand(String userId, PostPostingRequest postPostingRequest);

    PostPostingResponse dtoToPostResponse(PostPostingResponseDto postPostingResponseDto);

    DeletePostingCommand deleteRequestToCommand(Long postingId, String userId);

    DeletePostingResponse dtoToDeleteResponse(DeletePostingResponseDto deletePostingResponseDto);

    UpdatePostingCommand updateRequestToCommand(Long postingId, String userId, UpdatePostingRequest deletePostingCommand);

    UpdatePostingResponse dtoToUpdateResponse(UpdatePostingResponseDto updatePostingResponseDto);
}
