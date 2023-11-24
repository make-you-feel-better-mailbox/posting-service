package com.onetwo.postservice.adapter.in.web.posting.mapper;

import com.onetwo.postservice.adapter.in.web.posting.request.FilterSliceRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import org.springframework.data.domain.Slice;

public interface PostingFilterDtoMapper {
    PostingFilterCommand filterRequestToCommand(FilterSliceRequest filterSliceRequest);

    Slice<FilteredPostingResponse> dtoToFilteredPostingResponse(Slice<FilteredPostingResponseDto> filterPostingByUserDto);
}
