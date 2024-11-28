package com.onetwo.postservice.adapter.out.persistence.repository.posting;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import com.onetwo.postservice.application.port.in.command.PostingFilterCommand;

import java.util.List;

public interface QPostingRepository {
    List<PostingEntity> sliceByCommand(PostingFilterCommand postingFilterCommand);
}
