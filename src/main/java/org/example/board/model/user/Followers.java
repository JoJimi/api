package org.example.board.model.user;

import org.example.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record Followers(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime,
        ZonedDateTime followeredDateTime,
        Boolean isFollowing) {
    public static Followers from(User user, ZonedDateTime followeredDateTime){
        return new Followers(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createDateTime(),
                user.updateDateTime(),
                followeredDateTime,
                user.isFollowing());
    }
}
