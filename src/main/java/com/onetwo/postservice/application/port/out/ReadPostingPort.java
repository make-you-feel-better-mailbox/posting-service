package com.onetwo.postservice.application.port.out;

import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;
import com.onetwo.postservice.domain.Posting;

import java.util.List;
import java.util.Optional;

public interface ReadPostingPort {
    Optional<Posting> findById(Long postingId);

    List<Posting> filterPosting(PostingFilterCommand postingFilterCommand);
}
