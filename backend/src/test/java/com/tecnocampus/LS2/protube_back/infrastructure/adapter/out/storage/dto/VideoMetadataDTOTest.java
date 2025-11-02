package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.storage.dto;

import com.tecnocampus.LS2.protube_back.adapter.out.storage.dto.VideoMetadataDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoMetadataDTOTest {

    @Test
    void testGettersAndSetters() {
        VideoMetadataDTO dto = new VideoMetadataDTO();

        dto.setId(1L);
        dto.setTitle("Test Video");
        dto.setUser("testuser");
        dto.setWidth(1920);
        dto.setHeight(1080);
        dto.setDuration(120.5);
        dto.setTimestamp(1234567890L);

        assertEquals(1L, dto.getId());
        assertEquals("Test Video", dto.getTitle());
        assertEquals("testuser", dto.getUser());
        assertEquals(1920, dto.getWidth());
        assertEquals(1080, dto.getHeight());
        assertEquals(120.5, dto.getDuration());
        assertEquals(1234567890L, dto.getTimestamp());
    }

    @Test
    void testDefaultValues() {
        VideoMetadataDTO dto = new VideoMetadataDTO();

        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getUser());
        assertNull(dto.getWidth());
        assertNull(dto.getHeight());
        assertNull(dto.getDuration());
        assertNull(dto.getTimestamp());
    }
}
