package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoJpaRepository extends JpaRepository<VideoEntity, UUID> {
}
