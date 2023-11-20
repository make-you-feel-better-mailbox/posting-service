package com.onetwo.postservice.adapter.in.web.posting.api;

import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.PostPostingRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.PostPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.common.GlobalUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostingController {

    private final PostPostingUseCase postPostingUseCase;
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
}
