package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

import com.tecnocampus.LS2.protube_back.domain.model.User;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public User toDomain(UserEntity entity) {
        return User.from(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getDescription(),
                entity.getProfilePictureUrl(),
                entity.getBannerUrl()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.name(),
                user.surname(),
                user.email(),
                user.username(),
                user.hashedPassword(),
                user.description(),
                user.profilePictureUrl(),
                user.bannerUrl()
        );
    }
}
