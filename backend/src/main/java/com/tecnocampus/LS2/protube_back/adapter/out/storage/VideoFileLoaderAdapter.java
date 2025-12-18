package com.tecnocampus.LS2.protube_back.adapter.out.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.VideoLoaderPort;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.model.Channel;
import com.tecnocampus.LS2.protube_back.domain.model.Comment;
import com.tecnocampus.LS2.protube_back.adapter.out.storage.dto.VideoMetadataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            LOG.warn("Videos directory does not exist: {}", videosDirectory.toAbsolutePath());
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

        // Create Video with basic fields
        Video video = new Video();
        video.setTitle(metadata.getTitle());
        video.setUser(metadata.getUser());
        video.setVideoFileName(videoFileName);
        video.setThumbnailFileName(thumbnailFileName);
        video.setDuration(metadata.getDuration());
        video.setTimestamp(metadata.getTimestamp());

        // Load meta information if available
        if (metadata.getMeta() != null) {
            VideoMetadataDTO.MetaDTO meta = metadata.getMeta();
            video.setDescription(meta.getDescription());
            video.setViewCount(meta.getViewCount());
            video.setLikeCount(meta.getLikeCount());

            // Create Channel
            if (meta.getChannel() != null) {
                Channel channel = new Channel();
                channel.setName(meta.getChannel());
                channel.setFollowerCount(meta.getChannelFollowerCount());
                video.setChannel(channel);
            }

            // Create Comments
            if (meta.getComments() != null && !meta.getComments().isEmpty()) {
                List<Comment> comments = meta.getComments().stream()
                        .map(commentMeta -> {
                            Comment comment = new Comment();
                            comment.setAuthor(commentMeta.getAuthor());
                            comment.setText(commentMeta.getText());
                            comment.setTimestamp(commentMeta.getTimestamp());
                            comment.setLikeCount(commentMeta.getLikeCount());
                            return comment;
                        })
                        .collect(Collectors.toList());
                video.setComments(comments);
            }
        }

        return video;
    }
}
