package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoJpaRepository extends JpaRepository<VideoEntity, UUID> {
    @org.springframework.data.jpa.repository.Query("SELECT v FROM VideoEntity v WHERE " +
            "LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.user) LIKE LOWER(CONCAT('%', :query, '%'))")
    org.springframework.data.domain.Page<VideoEntity> findBySearchQuery(@org.springframework.data.repository.query.Param("query") String query, org.springframework.data.domain.Pageable pageable);
}
