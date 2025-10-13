package com.tecnocampus.LS2.protube_back.application.port.out;

import com.tecnocampus.LS2.protube_back.domain.model.Video;

import java.nio.file.Path;
import java.util.List;

/**
 * Output port for loading videos from external sources
 * This interface defines the contract for loading video metadata
 */
public interface VideoLoaderPort {

    /**
     * Load all videos from the specified directory
     *
     * @param videosDirectory path to the directory containing video files
     * @return list of Video domain entities
     */
    List<Video> loadVideosFromDirectory(Path videosDirectory);
}
