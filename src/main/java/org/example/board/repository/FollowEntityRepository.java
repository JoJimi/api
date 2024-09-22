package org.example.board.repository;

import org.example.board.model.entity.FollowEntity;
import org.example.board.model.entity.LikeEntity;
import org.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {
    List<FollowEntity> findByFollower(UserEntity follower);
    List<FollowEntity> findByFollowing(UserEntity following);
    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity follower, UserEntity following);
}
