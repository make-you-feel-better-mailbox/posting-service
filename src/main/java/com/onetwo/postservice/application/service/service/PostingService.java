package com.onetwo.postservice.application.service.service;

import com.onetwo.postservice.application.port.in.command.*;
import com.onetwo.postservice.application.port.in.response.*;
import com.onetwo.postservice.application.port.in.usecase.DeletePostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.ReadPosingUseCase;
import com.onetwo.postservice.application.port.in.usecase.UpdatePostingUseCase;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.application.port.out.UpdatePostingPort;
import com.onetwo.postservice.application.service.converter.PostingUseCaseConverter;
import com.onetwo.postservice.domain.Posting;
import lombok.RequiredArgsConstructor;
import onetwo.mailboxcommonconfig.common.exceptions.BadRequestException;
import onetwo.mailboxcommonconfig.common.exceptions.NotFoundResourceException;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostingService implements PostPostingUseCase, DeletePostingUseCase, UpdatePostingUseCase, ReadPosingUseCase {

    private final RegisterPostingPort registerPostingPort;
    private final ReadPostingPort readPostingPort;
    private final UpdatePostingPort updatePostingPort;
    private final PostingUseCaseConverter postingUseCaseConverter;

    /**
     * Post posting use case,
     * post posting data to persistence
     *
     * @param postPostingCommand data about request posting with user id
     * @return Boolean about post success and if success post, then return with saved posting's id
     */
    @Override
    @Transactional
    public PostPostingResponseDto postPosting(PostPostingCommand postPostingCommand) {
        Posting newPosting = Posting.createNewPostingByCommand(postPostingCommand);

        Posting savedPosting = registerPostingPort.registerPosting(newPosting);

        return postingUseCaseConverter.postingToPostResponseDto(savedPosting);
    }

    /**
     * Delete posting use case,
     * delete posting data to persistence
     *
     * @param deletePostingCommand request delete posting id and request user id
     * @return Boolean about delete posting success
     */
    @Override
    @Transactional
    public DeletePostingResponseDto deletePosting(DeletePostingCommand deletePostingCommand) {
        Posting posting = checkPostingExistAndGetPosting(deletePostingCommand.getPostingId());

        if (!posting.isSameUserId(deletePostingCommand.getUserId()))
            throw new BadRequestException("Poster does not match with request user");

        posting.deletePosting();

        updatePostingPort.updatePosting(posting);

        return postingUseCaseConverter.postingToDeleteResponseDto(posting);
    }

    /**
     * Update posting use case,
     * update posting data on persistence
     *
     * @param updatePostingCommand request update posting id and request user id and update data
     * @return Boolean about update posting success
     */
    @Override
    @Transactional
    public UpdatePostingResponseDto updatePosting(UpdatePostingCommand updatePostingCommand) {
        Posting posting = checkPostingExistAndGetPosting(updatePostingCommand.getPostingId());

        if (!posting.isSameUserId(updatePostingCommand.getUserId()))
            throw new BadRequestException("Poster does not match with request user");

        posting.updatePosting(updatePostingCommand);

        updatePostingPort.updatePosting(posting);

        return postingUseCaseConverter.postingToUpdateResponseDto(true);
    }

    /**
     * Get Detail about posting use case,
     * Get detail data about posting if exist
     *
     * @param findPostingDetailCommand Request posting id
     * @return Detail data about posting
     */
    @Override
    @Transactional(readOnly = true)
    public FindPostingDetailResponseDto findPostingDetail(FindPostingDetailCommand findPostingDetailCommand) {
        Posting posting = checkPostingExistAndGetPosting(findPostingDetailCommand.getPostingId());

        return postingUseCaseConverter.postingToDetailResponse(posting);
    }

    private Posting checkPostingExistAndGetPosting(Long postingId) {
        Optional<Posting> optionalPosting = readPostingPort.findById(postingId);

        if (optionalPosting.isEmpty()) throw new NotFoundResourceException("Posting dose not exist");

        Posting posting = optionalPosting.get();

        if (posting.isDeleted()) throw new BadRequestException("Posting already deleted");

        return posting;
    }

    /**
     * Get Filtered Posting use case,
     * Get Filtered slice posting data
     *
     * @param postingFilterCommand filter condition and pageable
     * @return content and slice data
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<FilteredPostingResponseDto> filterPosting(PostingFilterCommand postingFilterCommand) {
        List<Posting> postingList = readPostingPort.filterPosting(postingFilterCommand);

        boolean hasNext = postingList.size() > postingFilterCommand.getPageable().getPageSize();

        if (hasNext) postingList.remove(postingList.size() - 1);

        List<FilteredPostingResponseDto> filteredPostingResponseDtoList = postingList.stream()
                .map(postingUseCaseConverter::postingToFilteredResponse).toList();

        return new SliceImpl<>(filteredPostingResponseDtoList, postingFilterCommand.getPageable(), hasNext);
    }
}
