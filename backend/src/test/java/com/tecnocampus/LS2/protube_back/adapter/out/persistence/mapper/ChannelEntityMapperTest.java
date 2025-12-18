package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.ChannelEntity;
import com.tecnocampus.LS2.protube_back.domain.model.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChannelEntityMapperTest {

    private ChannelEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ChannelEntityMapper();
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        Channel channel = new Channel();
        java.util.UUID id = java.util.UUID.randomUUID();
        channel.setId(id);
        channel.setName("Test Channel");
        channel.setFollowerCount(100L);

        ChannelEntity entity = mapper.toEntity(channel);

        assertEquals(id, entity.getId());
        assertEquals("Test Channel", entity.getName());
        assertEquals(100L, entity.getFollowerCount());
    }

    @Test
    void toDomain_ShouldMapCorrectly() {
        ChannelEntity entity = new ChannelEntity();
        java.util.UUID id = java.util.UUID.randomUUID();
        entity.setId(id);
        entity.setName("Test Channel");
        entity.setFollowerCount(100L);

        Channel channel = mapper.toDomain(entity);

        assertEquals(id, channel.getId());
        assertEquals("Test Channel", channel.getName());
        assertEquals(100L, channel.getFollowerCount());
    }
}
