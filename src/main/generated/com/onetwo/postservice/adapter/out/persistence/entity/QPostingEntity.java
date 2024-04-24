package com.onetwo.postservice.adapter.out.persistence.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostingEntity is a Querydsl query type for PostingEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostingEntity extends EntityPathBase<PostingEntity> {

    private static final long serialVersionUID = 1843136060L;

    public static final QPostingEntity postingEntity = new QPostingEntity("postingEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath mediaExist = createBoolean("mediaExist");

    public final BooleanPath state = createBoolean("state");

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath userId = createString("userId");

    public QPostingEntity(String variable) {
        super(PostingEntity.class, forVariable(variable));
    }

    public QPostingEntity(Path<? extends PostingEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostingEntity(PathMetadata metadata) {
        super(PostingEntity.class, metadata);
    }

}

