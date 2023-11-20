package com.onetwo.postservice.application.service.adapter;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import com.onetwo.postservice.adapter.out.persistence.repository.posting.PostingRepository;
import com.onetwo.postservice.application.port.out.RegisterPostingPort;
import com.onetwo.postservice.domain.Posting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostingPersistenceAdapter implements RegisterPostingPort {

    private final PostingRepository postingRepository;

    @Override
    public Posting registerPosting(Posting newPosting) {
        PostingEntity postingEntity = PostingEntity.domainToEntity(newPosting);

        PostingEntity savedPostingEntity = postingRepository.save(postingEntity);

        return Posting.entityToDomain(savedPostingEntity);
    }
}
