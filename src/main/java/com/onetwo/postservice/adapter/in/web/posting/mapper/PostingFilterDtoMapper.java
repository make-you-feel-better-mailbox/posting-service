package com.onetwo.postservice.adapter.in.web.posting.mapper;

import com.onetwo.postservice.adapter.in.web.posting.request.PageRequestDto;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterByUserCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import org.springframework.data.domain.Slice;

public interface PostingFilterDtoMapper {
    PostingFilterByUserCommand filterByUserToCommand(String userId, PageRequestDto pageRequestDto);

    Slice<FilteredPostingResponse> dtoToFilteredPostingResponse(Slice<FilteredPostingResponseDto> filterPostingByUserDto);
}
