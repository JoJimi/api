package org.example.board.repository;

import org.example.board.model.entity.PostEntity;
import org.example.board.model.entity.ReplyEntity;
import org.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByUser(UserEntity user);
    List<ReplyEntity> findByPost(PostEntity user);
}
