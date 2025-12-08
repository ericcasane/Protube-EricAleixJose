package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.ChannelEntity;
import com.tecnocampus.LS2.protube_back.domain.model.Channel;
import org.springframework.stereotype.Component;

@Component
public class ChannelEntityMapper {

    public ChannelEntity toEntity(Channel channel) {
        ChannelEntity entity = new ChannelEntity();
        entity.setId(channel.getId());
        entity.setName(channel.getName());
        entity.setFollowerCount(channel.getFollowerCount());
        // No mappear videos para evitar referencias circulares
        return entity;
    }

    public Channel toDomain(ChannelEntity entity) {
        Channel channel = new Channel();
        channel.setId(entity.getId());
        channel.setName(entity.getName());
        channel.setFollowerCount(entity.getFollowerCount());
        // No mappear videos para evitar referencias circulares
        return channel;
    }
}
