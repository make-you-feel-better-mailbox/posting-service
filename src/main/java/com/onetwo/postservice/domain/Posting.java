package com.onetwo.postservice.domain;

import com.onetwo.postservice.adapter.out.persistence.entity.PostingEntity;
import com.onetwo.postservice.application.port.in.command.PostPostingCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Posting extends BaseDomain {

    private Long id;
    private String userId;

    private String content;

    private Boolean state;

    public static Posting createNewPostingByCommand(PostPostingCommand postPostingCommand) {
        Posting posting = new Posting(
                null,
                postPostingCommand.getUserId(),
                postPostingCommand.getContent(),
                false);

        posting.setDefaultState();

        return posting;
    }

    public static Posting entityToDomain(PostingEntity savedPostingEntity) {
        Posting posting = new Posting(
                savedPostingEntity.getId(),
                savedPostingEntity.getUserId(),
                savedPostingEntity.getContent(),
                savedPostingEntity.getState()
        );

        posting.setMetaDataByEntity(savedPostingEntity);

        return posting;
    }

    private void setDefaultState() {
        setCreatedAt(Instant.now());
        setCreateUser(this.userId);
    }

    public boolean isNotDeleted() {
        return !this.state;
    }

    public boolean isDeleted() {
        return this.state;
    }

    public boolean isSameUserId(String userId) {
        return this.userId.equals(userId);
    }

    public void deletePosting() {
        this.state = true;
    }
}
