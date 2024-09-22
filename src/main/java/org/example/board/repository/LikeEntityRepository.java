package org.example.board.repository;

import org.example.board.model.entity.LikeEntity;
import org.example.board.model.entity.PostEntity;
import org.example.board.model.entity.ReplyEntity;
import org.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    List<LikeEntity> findByUser(UserEntity user);
    List<LikeEntity> findByPost(PostEntity user);
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
