package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.UpdatePostingCommand;
import com.onetwo.postservice.application.port.in.response.UpdatePostingResponseDto;

public interface UpdatePostingUseCase {

    /**
     * Update posting use case,
     * update posting data on persistence
     *
     * @param updatePostingCommand request update posting id and request user id and update data
     * @return Boolean about update posting success
     */
    UpdatePostingResponseDto updatePosting(UpdatePostingCommand updatePostingCommand);
}
