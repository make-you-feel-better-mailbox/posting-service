package com.onetwo.postservice.application.service.adapter;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import com.onetwo.postservice.adapter.out.persistence.repository.posting.PostingRepository;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.application.port.out.ReadPostingPort;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.application.port.out.UpdatePostingPort;
import com.onetwo.postservice.domain.Posting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostingPersistenceAdapter implements RegisterPostingPort, ReadPostingPort, UpdatePostingPort {

    private final PostingRepository postingRepository;

    @Override
    public Posting registerPosting(Posting newPosting) {
        PostingEntity postingEntity = PostingEntity.domainToEntity(newPosting);

        PostingEntity savedPostingEntity = postingRepository.save(postingEntity);

        return Posting.entityToDomain(savedPostingEntity);
    }

    @Override
    public Optional<Posting> findById(Long postingId) {
        Optional<PostingEntity> optionalPostingEntity = postingRepository.findById(postingId);

        if (optionalPostingEntity.isPresent()) {
            Posting posting = Posting.entityToDomain(optionalPostingEntity.get());

            return Optional.of(posting);
        }

        return Optional.empty();
    }

    @Override
    public List<Posting> filterPosting(PostingFilterCommand postingFilterCommand) {
        List<PostingEntity> postingEntityList = postingRepository.sliceByCommand(postingFilterCommand);

        return postingEntityList.stream().map(Posting::entityToDomain).collect(Collectors.toList());
    }

    @Override
    public void updatePosting(Posting posting) {
        PostingEntity postingEntity = PostingEntity.domainToEntity(posting);

        postingRepository.save(postingEntity);
    }
}
