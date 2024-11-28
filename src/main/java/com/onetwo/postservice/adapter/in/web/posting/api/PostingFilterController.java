package com.onetwo.postservice.adapter.in.web.posting.api;

import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingFilterDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.FilterSliceRequest;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.ReadPosingUseCase;
import com.onetwo.postservice.common.GlobalUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostingFilterController {

    private final ReadPosingUseCase readPosingUseCase;
    private final PostingFilterDtoMapper postingFilterDtoMapper;

    /**
     * Get Filtered Posting inbound adapter
     *
     * @param filterSliceRequest filter condition and pageable
     * @return content and slice data
     */
    @GetMapping(GlobalUrl.POSTING_FILTER)
    public ResponseEntity<Slice<FilteredPostingResponse>> filterPosting(@ModelAttribute FilterSliceRequest filterSliceRequest) {
        PostingFilterCommand postingFilterCommand = postingFilterDtoMapper.filterRequestToCommand(filterSliceRequest);
        Slice<FilteredPostingResponseDto> filterPostingByUserDto = readPosingUseCase.filterPosting(postingFilterCommand);
        return ResponseEntity.ok().body(postingFilterDtoMapper.dtoToFilteredPostingResponse(filterPostingByUserDto));
    }
}
