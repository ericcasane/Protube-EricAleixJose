package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;

import com.tecnocampus.LS2.protube_back.domain.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class VideoEntityMapper {

    @Autowired
    private ChannelEntityMapper channelMapper;

    @Autowired
    private CommentEntityMapper commentMapper;

    public VideoEntity toEntity(Video video) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(video.getTitle());
        entity.setUser(video.getUser());
        entity.setVideoFileName(video.getVideoFileName());
        entity.setThumbnailFileName(video.getThumbnailFileName());
        entity.setDuration(video.getDuration());
        entity.setDescription(video.getDescription());
        entity.setTimestamp(video.getTimestamp());
        entity.setViewCount(video.getViewCount());
        entity.setLikeCount(video.getLikeCount());

        if (video.getChannel() != null) {
            entity.setChannel(channelMapper.toEntity(video.getChannel()));
        }

        if (video.getComments() != null) {
            entity.setComments(video.getComments().stream()
                    .map(commentMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    public Video toDomain(VideoEntity entity) {
        Video video = new Video();
        video.setTitle(entity.getTitle());
        video.setUser(entity.getUser());
        video.setVideoFileName(entity.getVideoFileName());
        video.setThumbnailFileName(entity.getThumbnailFileName());
        video.setDuration(entity.getDuration());
        video.setDescription(entity.getDescription());
        video.setTimestamp(entity.getTimestamp());
        video.setViewCount(entity.getViewCount());
        video.setLikeCount(entity.getLikeCount());

        if (entity.getChannel() != null) {
            video.setChannel(channelMapper.toDomain(entity.getChannel()));
        }

        if (entity.getComments() != null) {
            video.setComments(entity.getComments().stream()
                    .map(commentMapper::toDomain)
                    .collect(Collectors.toList()));
        }

        return video;
    }

}
