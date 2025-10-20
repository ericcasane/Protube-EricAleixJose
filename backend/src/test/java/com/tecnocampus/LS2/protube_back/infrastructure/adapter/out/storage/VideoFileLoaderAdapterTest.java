package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.storage.dto.VideoMetadataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoFileLoaderAdapterTest {

    private VideoFileLoaderAdapter adapter;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        adapter = new VideoFileLoaderAdapter(objectMapper);
    }

    @Test
    void testLoadVideosFromNonExistentDirectory() {
        Path nonExistentPath = tempDir.resolve("nonexistent");

        List<Video> videos = adapter.loadVideosFromDirectory(nonExistentPath);

        assertNotNull(videos);
        assertTrue(videos.isEmpty());
    }

    @Test
    void testLoadVideosFromFile() throws IOException {
        Path filePath = tempDir.resolve("file.txt");
        Files.createFile(filePath);

        List<Video> videos = adapter.loadVideosFromDirectory(filePath);

        assertNotNull(videos);
        assertTrue(videos.isEmpty());
    }

    @Test
    void testLoadVideosFromEmptyDirectory() {
        List<Video> videos = adapter.loadVideosFromDirectory(tempDir);

        assertNotNull(videos);
        assertTrue(videos.isEmpty());
    }

    @Test
    void testLoadVideosFromDirectoryWithValidJsonFile() throws IOException {
        VideoMetadataDTO metadata = new VideoMetadataDTO();
        metadata.setId(1L);
        metadata.setTitle("Test Video");
        metadata.setUser("testuser");
        metadata.setWidth(1920);
        metadata.setHeight(1080);
        metadata.setDuration(120.5);
        metadata.setTimestamp(1234567890L);

        Path jsonFile = tempDir.resolve("video1.json");
        objectMapper.writeValue(jsonFile.toFile(), metadata);

        List<Video> videos = adapter.loadVideosFromDirectory(tempDir);

        assertNotNull(videos);
        assertEquals(1, videos.size());
        Video video = videos.get(0);
        assertEquals("Test Video", video.getTitle());
        assertEquals("testuser", video.getUser());
        assertEquals("video1.mp4", video.getVideoFileName());
        assertEquals("video1.webp", video.getThumbnailFileName());
    }

    @Test
    void testLoadVideosFromDirectoryWithMultipleJsonFiles() throws IOException {
        VideoMetadataDTO metadata1 = new VideoMetadataDTO();
        metadata1.setTitle("Video 1");
        metadata1.setUser("user1");

        VideoMetadataDTO metadata2 = new VideoMetadataDTO();
        metadata2.setTitle("Video 2");
        metadata2.setUser("user2");

        Path jsonFile1 = tempDir.resolve("video1.json");
        Path jsonFile2 = tempDir.resolve("video2.json");
        objectMapper.writeValue(jsonFile1.toFile(), metadata1);
        objectMapper.writeValue(jsonFile2.toFile(), metadata2);

        List<Video> videos = adapter.loadVideosFromDirectory(tempDir);

        assertNotNull(videos);
        assertEquals(2, videos.size());
    }

    @Test
    void testLoadVideosFromDirectoryWithInvalidJsonFile() throws IOException {
        Path invalidJsonFile = tempDir.resolve("invalid.json");
        Files.writeString(invalidJsonFile, "{invalid json content}");

        List<Video> videos = adapter.loadVideosFromDirectory(tempDir);

        assertNotNull(videos);
        assertTrue(videos.isEmpty());
    }
}
