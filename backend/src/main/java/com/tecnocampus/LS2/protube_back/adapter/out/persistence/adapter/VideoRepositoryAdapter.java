package com.tecnocampus.LS2.protube_back.adapter.out.persistence.adapter;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.VideoRepository;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper.VideoEntityMapper;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.VideoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoRepositoryAdapter implements VideoRepository {

    private final VideoJpaRepository jpaRepository;
    private final VideoEntityMapper mapper;

    public VideoRepositoryAdapter(VideoJpaRepository jpaRepository, VideoEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Video save(Video video) {
        VideoEntity entity = mapper.toEntity(video);
        VideoEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<Video> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
