package org.example.board.model.user;

import org.example.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record LikedUser(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createDateTime,
        ZonedDateTime updateDateTime,
        Boolean isFollowing,
        Long likedPostId,
        ZonedDateTime likedDateTime) {
    public static LikedUser from(User user, Long likedPostId, ZonedDateTime likedDateTime){
        return new LikedUser(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createDateTime(),
                user.updateDateTime(),
                user.isFollowing(),
                likedPostId,
                likedDateTime
        );
    }
}
