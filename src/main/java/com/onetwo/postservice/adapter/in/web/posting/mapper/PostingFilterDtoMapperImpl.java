package com.onetwo.postservice.adapter.in.web.posting.mapper;

import com.onetwo.postservice.adapter.in.web.posting.request.PageRequestDto;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterByUserCommand;
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
    public PostingFilterByUserCommand filterByUserToCommand(String userId, PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
                pageRequestDto.pageNumber() == null ? 0 : pageRequestDto.pageNumber(),
                pageRequestDto.pageSize() == null ? 20 : pageRequestDto.pageSize()
        );
        return new PostingFilterByUserCommand(userId, pageable);
    }

    @Override
    public Slice<FilteredPostingResponse> dtoToFilteredPostingResponse(Slice<FilteredPostingResponseDto> filterPostingByUserDto) {
        List<FilteredPostingResponse> filteredPostingResponseList = filterPostingByUserDto.getContent().stream()
                .map(response -> new FilteredPostingResponse(response.postingId(), response.userId(), response.postedDate())).toList();

        return new SliceImpl<>(filteredPostingResponseList, filterPostingByUserDto.getPageable(), filterPostingByUserDto.hasNext());
    }
}
