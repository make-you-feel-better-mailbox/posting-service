package com.onetwo.postservice.application.port.out;

import com.onetwo.postservice.domain.Posting;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReadPostingPort {
    Optional<Posting> findById(Long postingId);

    List<Posting> findByUserId(String userId, Pageable pageable);
}
