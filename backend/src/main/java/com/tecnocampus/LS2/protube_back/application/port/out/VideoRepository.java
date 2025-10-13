package com.tecnocampus.LS2.protube_back.application.port.out;

import com.tecnocampus.LS2.protube_back.domain.model.Video;

import java.util.List;

public interface VideoRepository {

    Video save(Video video);

    List<Video> findAll();
}
