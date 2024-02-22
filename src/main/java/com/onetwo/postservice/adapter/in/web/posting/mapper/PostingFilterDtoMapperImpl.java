package com.onetwo.postservice.adapter.in.web.posting.mapper;

import com.onetwo.postservice.adapter.in.web.posting.request.FilterSliceRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostingFilterDtoMapperImpl implements PostingFilterDtoMapper {
    @Override
    public PostingFilterCommand filterRequestToCommand(FilterSliceRequest filterSliceRequest) {
        Pageable pageable = PageRequest.of(
                filterSliceRequest.pageNumber() == null ? 0 : filterSliceRequest.pageNumber(),
                filterSliceRequest.pageSize() == null ? 20 : filterSliceRequest.pageSize()
        );
        return new PostingFilterCommand(
                filterSliceRequest.userId(),
                filterSliceRequest.content(),
                filterSliceRequest.filterStartDate(),
                filterSliceRequest.filterEndDate(),
                pageable);
    }

    @Override
    public Slice<FilteredPostingResponse> dtoToFilteredPostingResponse(Slice<FilteredPostingResponseDto> filterPostingByUserDto) {
        List<FilteredPostingResponse> filteredPostingResponseList = filterPostingByUserDto.getContent().stream()
                .map(response -> new FilteredPostingResponse(response.postingId(), response.userId(), response.content(), response.mediaExist(), response.postedDate())).toList();

        return new SliceImpl<>(filteredPostingResponseList, filterPostingByUserDto.getPageable(), filterPostingByUserDto.hasNext());
    }
}
