package com.onetwo.postservice.application.service.service;

import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.application.service.converter.PostingUseCaseConverter;
import com.onetwo.postservice.domain.Posting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostingService implements PostPostingUseCase {

    private final RegisterPostingPort registerPostingPort;
    private final PostingUseCaseConverter postingUseCaseConverter;

    /**
     * Post posting use case,
     * post posting data to persistence
     *
     * @param postPostingCommand data about request posting with user id
     * @return Boolean about post success and if success post, then return with saved posting's id
     */
    @Override
    public PostPostingResponseDto postPosting(PostPostingCommand postPostingCommand) {
        Posting newPosting = Posting.createNewPostingByCommand(postPostingCommand);

        Posting savedPosting = registerPostingPort.registerPosting(newPosting);

        return postingUseCaseConverter.postingToPostResponseDto(savedPosting);
    }
}
