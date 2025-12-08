package com.tecnocampus.LS2.protube_back.adapter.out.persistence.adapter;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.VideoRepository;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.ChannelEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper.VideoEntityMapper;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.VideoJpaRepository;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.ChannelJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoRepositoryAdapter implements VideoRepository {

    private final VideoJpaRepository videoJpaRepository;
    private final ChannelJpaRepository channelJpaRepository;
    private final VideoEntityMapper mapper;

    public VideoRepositoryAdapter(VideoJpaRepository videoJpaRepository,
                                  ChannelJpaRepository channelJpaRepository,
                                  VideoEntityMapper mapper) {
        this.videoJpaRepository = videoJpaRepository;
        this.channelJpaRepository = channelJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Video save(Video video) {
        VideoEntity entity = mapper.toEntity(video);

        // Guardar o recuperar el canal primero (fuera de cascade)
        if (entity.getChannel() != null) {
            ChannelEntity channelEntity = channelJpaRepository
                    .findByName(entity.getChannel().getName())
                    .orElseGet(() -> channelJpaRepository.save(entity.getChannel()));
            entity.setChannel(channelEntity);
        }

        // Si hay comentarios, asegurar que tienen referencia al video
        if (entity.getComments() != null && !entity.getComments().isEmpty()) {
            for (com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.CommentEntity comment : entity.getComments()) {
                comment.setVideo(entity);
            }
        }

        VideoEntity savedEntity = videoJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<Video> findAll() {
        return videoJpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<Video> searchVideos(String query, org.springframework.data.domain.Pageable pageable) {
        return videoJpaRepository.findBySearchQuery(query, pageable)
                .map(mapper::toDomain);
    }
}
