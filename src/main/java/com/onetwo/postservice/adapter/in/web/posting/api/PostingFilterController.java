package com.onetwo.postservice.adapter.in.web.posting.api;

import com.onetwo.postservice.adapter.in.web.posting.mapper.PostingFilterDtoMapper;
import com.onetwo.postservice.adapter.in.web.posting.request.PageRequestDto;
import com.onetwo.postservice.adapter.in.web.posting.response.FilteredPostingResponse;
import com.onetwo.postservice.application.port.in.command.PostingFilterByUserCommand;
import com.onetwo.postservice.application.port.in.response.FilteredPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.ReadPosingUseCase;
import com.onetwo.postservice.common.GlobalUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostingFilterController {

    private final ReadPosingUseCase readPosingUseCase;
    private final PostingFilterDtoMapper postingFilterDtoMapper;

    /**
     * Get Filtered Posting by user id inbound adapter
     *
     * @param userId         filter condition user id
     * @param pageRequestDto pageable data
     * @return content and slice data
     */
    @GetMapping(GlobalUrl.POSTING_FILTER + GlobalUrl.PATH_VARIABLE_USER_ID_WITH_BRACE)
    public ResponseEntity<Slice<FilteredPostingResponse>> filterPostingByUser(@PathVariable(GlobalUrl.PATH_VARIABLE_USER_ID) String userId,
                                                                              @ModelAttribute PageRequestDto pageRequestDto) {
        PostingFilterByUserCommand postingFilterByUserCommand = postingFilterDtoMapper.filterByUserToCommand(userId, pageRequestDto);
        Slice<FilteredPostingResponseDto> filterPostingByUserDto = readPosingUseCase.filterPostingByUser(postingFilterByUserCommand);
        return ResponseEntity.ok().body(postingFilterDtoMapper.dtoToFilteredPostingResponse(filterPostingByUserDto));
    }
}
