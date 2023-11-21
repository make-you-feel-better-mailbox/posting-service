package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;

public interface DeletePostingUseCase {

    /**
     * Delete posting use case,
     * delete posting data to persistence
     *
     * @param deletePostingCommand request delete posting id and request user id
     * @return Boolean about delete posting success
     */
    DeletePostingResponseDto deletePosting(DeletePostingCommand deletePostingCommand);
}
