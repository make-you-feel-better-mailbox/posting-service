package com.onetwo.postservice.adapter.out.persistence.entity;

import com.onetwo.postservice.adapter.out.persistence.repository.converter.BooleanNumberConverter;
import com.onetwo.postservice.domain.Posting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
@Table(name = "posting")
public class PostingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 1)
    @Convert(converter = BooleanNumberConverter.class)
    private Boolean mediaExist;

    @Column(nullable = false, length = 1)
    @Convert(converter = BooleanNumberConverter.class)
    private Boolean state;

    private PostingEntity(Long id, String userId, String content, Boolean mediaExist, Boolean state) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.mediaExist = mediaExist;
        this.state = state;
    }

    public static PostingEntity domainToEntity(Posting posting) {
        PostingEntity postingEntity = new PostingEntity(
                posting.getId(),
                posting.getUserId(),
                posting.getContent(),
                posting.isMediaExist(),
                posting.isState()
        );

        postingEntity.setMetaDataByDomain(posting);
        return postingEntity;
    }
}
