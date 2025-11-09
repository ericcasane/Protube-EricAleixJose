package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.CommentEntity;
import com.tecnocampus.LS2.protube_back.domain.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentEntityMapper {

    public CommentEntity toEntity(Comment comment) {
        CommentEntity entity = new CommentEntity();
        entity.setId(comment.getId() != null ? java.util.UUID.fromString(comment.getId()) : null);
        entity.setText(comment.getText());
        entity.setAuthor(comment.getAuthor());
        entity.setTimestamp(comment.getTimestamp());
        entity.setLikeCount(comment.getLikeCount());
        // No mappear video para evitar referencias circulares
        return entity;
    }

    public Comment toDomain(CommentEntity entity) {
        Comment comment = new Comment();
        comment.setId(entity.getId() != null ? entity.getId().toString() : null);
        comment.setText(entity.getText());
        comment.setAuthor(entity.getAuthor());
        comment.setTimestamp(entity.getTimestamp());
        comment.setLikeCount(entity.getLikeCount());
        // No mappear video para evitar referencias circulares
        return comment;
    }
}
