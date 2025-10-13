package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.application.port.out.VideoLoaderPort;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.storage.dto.VideoMetadataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that implements VideoLoaderPort for reading videos from the filesystem
 * Loads video metadata from JSON files in the videos directory
 */
@Component
public class VideoFileLoaderAdapter implements VideoLoaderPort {

    private static final Logger LOG = LoggerFactory.getLogger(VideoFileLoaderAdapter.class);
    private final ObjectMapper objectMapper;

    public VideoFileLoaderAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Video> loadVideosFromDirectory(Path videosDirectory) {
        List<Video> videos = new ArrayList<>();

        if (!Files.exists(videosDirectory)) {
            LOG.warn("Videos directory does not exist: {}", videosDirectory);
            return videos;
        }

        if (!Files.isDirectory(videosDirectory)) {
            LOG.warn("Path is not a directory: {}", videosDirectory);
            return videos;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(videosDirectory, "*.json")) {
            for (Path jsonFile : stream) {
                try {
                    Video video = loadVideoFromJsonFile(jsonFile);
                    videos.add(video);
                    LOG.debug("Loaded video: {}", video.getTitle());
                } catch (IOException e) {
                    LOG.error("Failed to load video from file: {}", jsonFile, e);
                }
            }
        } catch (IOException e) {
            LOG.error("Failed to read videos directory: {}", videosDirectory, e);
        }

        LOG.info("Loaded {} videos from directory: {}", videos.size(), videosDirectory);
        return videos;
    }

    private Video loadVideoFromJsonFile(Path jsonFile) throws IOException {
        VideoMetadataDTO metadata = objectMapper.readValue(jsonFile.toFile(), VideoMetadataDTO.class);

        // Extract filename without extension
        String fileName = jsonFile.getFileName().toString();
        String videoId = fileName.substring(0, fileName.lastIndexOf('.'));

        // Build file names
        String videoFileName = videoId + ".mp4";
        String thumbnailFileName = videoId + ".webp";

        return new Video(
                metadata.getId(),
                metadata.getTitle(),
                metadata.getUser(),
                videoFileName,
                thumbnailFileName
        );
    }
}
