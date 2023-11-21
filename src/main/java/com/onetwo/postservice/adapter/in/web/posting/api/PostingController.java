package com.onetwo.postservice.adapter.in.web.posting.api;

import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingDtoMapper;
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
import com.onetwo.postservice.application.port.in.usecase.DeletePostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.UpdatePostingUseCase;
import com.onetwo.postservice.common.GlobalUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostingController {

    private final PostPostingUseCase postPostingUseCase;
    private final DeletePostingUseCase deletePostingUseCase;
    private final UpdatePostingUseCase updatePostingUseCase;
    private final PostingDtoMapper postingDtoMapper;

    /**
     * Post Posting inbound adapter
     *
     * @param postPostingRequest data about request posting
     * @param userId             user authentication id
     * @return Boolean about post success and if success post, then return with saved posting's id
     */
    @PostMapping(GlobalUrl.POST_ROOT)
    public ResponseEntity<PostPostingResponse> postPosting(@RequestBody @Valid PostPostingRequest postPostingRequest, @AuthenticationPrincipal String userId) {
        PostPostingCommand postPostingCommand = postingDtoMapper.postRequestToCommand(userId, postPostingRequest);
        PostPostingResponseDto postPostingResponseDto = postPostingUseCase.postPosting(postPostingCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(postingDtoMapper.dtoToPostResponse(postPostingResponseDto));
    }

    /**
     * Delete Posting inbound adapter
     *
     * @param postingId Request delete posting id
     * @param userId    user authentication id
     * @return Boolean about delete posting success
     */
    @DeleteMapping(GlobalUrl.POST_ROOT + "/{posting-id}")
    public ResponseEntity<DeletePostingResponse> deletePosting(@PathVariable("posting-id") Long postingId, @AuthenticationPrincipal String userId) {
        DeletePostingCommand deletePostingCommand = postingDtoMapper.deleteRequestToCommand(postingId, userId);
        DeletePostingResponseDto deletePostingResponseDto = deletePostingUseCase.deletePosting(deletePostingCommand);
        return ResponseEntity.ok().body(postingDtoMapper.dtoToDeleteResponse(deletePostingResponseDto));
    }

    /**
     * Update Posting inbound adapter
     *
     * @param postingId            Request update posting id
     * @param updatePostingRequest Request Update data about posting
     * @param userId               user authentication id
     * @return Boolean about update posting success
     */
    @PutMapping(GlobalUrl.POST_ROOT + "/{posting-id}")
    public ResponseEntity<UpdatePostingResponse> updatePosting(@PathVariable("posting-id") Long postingId,
                                                               @RequestBody @Valid UpdatePostingRequest updatePostingRequest,
                                                               @AuthenticationPrincipal String userId) {
        UpdatePostingCommand updatePostingCommand = postingDtoMapper.updateRequestToCommand(postingId, userId, updatePostingRequest);
        UpdatePostingResponseDto updatePostingResponseDto = updatePostingUseCase.updatePosting(updatePostingCommand);
        return ResponseEntity.ok().body(postingDtoMapper.dtoToUpdateResponse(updatePostingResponseDto));
    }
}
