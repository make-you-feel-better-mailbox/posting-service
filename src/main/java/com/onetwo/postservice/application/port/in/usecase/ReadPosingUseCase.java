package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;

public interface ReadPosingUseCase {

    /**
     * Get Detail about posting use case,
     * Get detail data about posting if exist
     *
     * @param findPostingDetailCommand Request posting id
     * @return Detail data about posting
     */
    FindPostingDetailResponseDto findPostingDetail(FindPostingDetailCommand findPostingDetailCommand);
}
