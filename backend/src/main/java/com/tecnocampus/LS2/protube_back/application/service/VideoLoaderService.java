package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.VideoLoaderPort;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class VideoLoaderService {

    private static final Logger LOG = LoggerFactory.getLogger(VideoLoaderService.class);
    private final VideoLoaderPort videoLoaderPort;

    public VideoLoaderService(VideoLoaderPort videoLoaderPort) {
        this.videoLoaderPort = videoLoaderPort;
    }

    /**
     * Load all videos from the specified directory
     *
     * @param videosDirectory path to the videos directory
     * @return list of loaded videos
     */
    public List<Video> loadVideos(Path videosDirectory) {
        LOG.info("Loading videos from directory: {}", videosDirectory);
        List<Video> videos = videoLoaderPort.loadVideosFromDirectory(videosDirectory);
        LOG.info("Successfully loaded {} videos", videos.size());
        return videos;
    }

    /**
     * Display videos information to console
     *
     * @param videos list of videos to display
     */
    public void displayVideos(List<Video> videos) {
        if (videos.isEmpty()) {
            LOG.info("No videos to display");
            return;
        }

        LOG.info("==================== VIDEO LIST ====================");
        LOG.info("Total videos loaded: {}", videos.size());
        LOG.info("====================================================");

        for (Video video : videos) {
            LOG.info("");
            LOG.info("Title: {}", video.getTitle());
            LOG.info("User: {}", video.getUser());
            LOG.info("Video File: {}", video.getVideoFileName());
            LOG.info("Thumbnail: {}", video.getThumbnailFileName());
            LOG.info("----------------------------------------------------");
        }

        LOG.info("====================================================");
    }
}
