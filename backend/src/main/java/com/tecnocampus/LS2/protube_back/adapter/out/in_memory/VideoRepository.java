package com.tecnocampus.LS2.protube_back.adapter.out.in_memory;

import com.tecnocampus.LS2.protube_back.domain.model.Video;

import java.util.List;

public interface VideoRepository {

    Video save(Video video);

    List<Video> findAll();

    org.springframework.data.domain.Page<Video> searchVideos(String query, org.springframework.data.domain.Pageable pageable);
}
