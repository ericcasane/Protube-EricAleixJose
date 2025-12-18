package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.UUID;

@Repository
public interface ChannelJpaRepository extends JpaRepository<ChannelEntity, UUID> {
    Optional<ChannelEntity> findByName(String name);
}
