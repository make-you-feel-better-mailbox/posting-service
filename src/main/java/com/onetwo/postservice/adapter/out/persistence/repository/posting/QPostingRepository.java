package com.onetwo.postservice.adapter.out.persistence.repository.posting;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QPostingRepository {
    List<PostingEntity> sliceByUserId(String userId, Pageable pageable);
}
