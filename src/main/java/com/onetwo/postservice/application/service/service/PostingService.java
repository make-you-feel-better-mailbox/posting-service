package com.onetwo.postservice.application.service.service;

import com.onetwo.postservice.application.port.in.command.DeletePostingCommand;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import com.onetwo.postservice.application.port.in.response.DeletePostingResponseDto;
import com.onetwo.postservice.application.port.in.response.PostPostingResponseDto;
import com.onetwo.postservice.application.port.in.usecase.DeletePostingUseCase;
import com.onetwo.postservice.application.port.in.usecase.PostPostingUseCase;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.application.port.out.UpdatePostingPort;
import com.onetwo.postservice.application.service.converter.PostingUseCaseConverter;
import com.onetwo.postservice.common.exceptions.BadRequestException;
import com.onetwo.postservice.common.exceptions.NotFoundResourceException;
import com.onetwo.postservice.domain.Posting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostingService implements PostPostingUseCase, DeletePostingUseCase {

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

    private Posting checkPostingExistAndGetPosting(Long postingId) {
        Optional<Posting> optionalPosting = readPostingPort.findById(postingId);

        if (optionalPosting.isEmpty()) throw new NotFoundResourceException("Posting dose not exist");

        if (optionalPosting.get().isDeleted()) throw new BadRequestException("Posting already deleted");

        return optionalPosting.get();
    }
}
