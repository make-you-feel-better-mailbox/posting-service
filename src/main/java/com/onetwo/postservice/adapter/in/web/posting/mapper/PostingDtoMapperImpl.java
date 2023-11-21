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
import org.springframework.stereotype.Component;

@Component
public class PostingDtoMapperImpl implements PostingDtoMapper {
    @Override
    public PostPostingCommand postRequestToCommand(String userId, PostPostingRequest postPostingRequest) {
        return new PostPostingCommand(userId, postPostingRequest.content());
    }

    @Override
    public PostPostingResponse dtoToPostResponse(PostPostingResponseDto postPostingResponseDto) {
        return new PostPostingResponse(postPostingResponseDto.postingId(), postPostingResponseDto.isPostSuccess());
    }

    @Override
    public DeletePostingCommand deleteRequestToCommand(Long postingId, String userId) {
        return new DeletePostingCommand(postingId, userId);
    }

    @Override
    public DeletePostingResponse dtoToDeleteResponse(DeletePostingResponseDto deletePostingResponseDto) {
        return new DeletePostingResponse(deletePostingResponseDto.isDeleteSuccess());
    }

    @Override
    public UpdatePostingCommand updateRequestToCommand(Long postingId, String userId, UpdatePostingRequest deletePostingCommand) {
        return new UpdatePostingCommand(postingId, userId, deletePostingCommand.content());
    }

    @Override
    public UpdatePostingResponse dtoToUpdateResponse(UpdatePostingResponseDto updatePostingResponseDto) {
        return new UpdatePostingResponse(updatePostingResponseDto.isUpdateSuccess());
    }
}
