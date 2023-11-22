package com.onetwo.postservice.application.port.in.usecase;

import com.onetwo.postservice.application.port.in.command.FindPostingDetailCommand;
import com.onetwo.postservice.application.port.in.command.PostingFilterByUserCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.response.FindPostingDetailResponseDto;
import org.springframework.data.domain.Slice;

public interface ReadPosingUseCase {

    /**
     * Get Detail about posting use case,
     * Get detail data about posting if exist
     *
     * @param findPostingDetailCommand Request posting id
     * @return Detail data about posting
     */
    FindPostingDetailResponseDto findPostingDetail(FindPostingDetailCommand findPostingDetailCommand);

    /**
     * Get Filtered Posting by user id use case,
     * Get Filtered slice posting data
     *
     * @param postingFilterByUserCommand filter condition user id and pageable
     * @return content and slice data
     */
    Slice<FilteredPostingResponseDto> filterPostingByUser(PostingFilterByUserCommand postingFilterByUserCommand);
}
