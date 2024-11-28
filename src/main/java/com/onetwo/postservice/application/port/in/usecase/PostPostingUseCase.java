package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;

public interface PostPostingUseCase {

    /**
     * Post posting use case,
     * post posting data to persistence
     *
     * @param postPostingCommand data about request posting with user id
     * @return Boolean about post success and if success post, then return with saved posting's id
     */
    PostPostingResponseDto postPosting(PostPostingCommand postPostingCommand);
}
