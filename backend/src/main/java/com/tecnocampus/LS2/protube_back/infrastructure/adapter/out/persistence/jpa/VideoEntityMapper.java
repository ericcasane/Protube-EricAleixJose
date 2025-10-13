package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.persistence.jpa;

import com.tecnocampus.LS2.protube_back.domain.model.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoEntityMapper {

    public VideoEntity toEntity(Video video) {
        return new VideoEntity(
                video.getTitle(),
                video.getUser(),
                video.getVideoFileName(),
                video.getThumbnailFileName()
        );
    }

    public Video toDomain(VideoEntity entity) {
        return new Video(
                entity.getTitle(),
                entity.getUser(),
                entity.getVideoFileName(),
                entity.getThumbnailFileName()
        );
    }
}
