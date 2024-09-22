package org.example.board.service;

import org.example.board.exception.post.PostNotFoundException;
import org.example.board.exception.reply.ReplyNotFoundException;
import org.example.board.exception.user.UserNotAllowedException;
import org.example.board.exception.user.UserNotFoundException;
import org.example.board.model.entity.ReplyEntity;
import org.example.board.model.entity.UserEntity;
import org.example.board.model.reply.Reply;
import org.example.board.model.reply.ReplyPatchRequestBody;
import org.example.board.model.reply.ReplyPostRequestBody;
import org.example.board.repository.PostEntityRepository;
import org.example.board.repository.ReplyEntityRepository;
import org.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ReplyService {

    @Autowired private ReplyEntityRepository replyEntityRepository;
    @Autowired private PostEntityRepository postEntityRepository;
    @Autowired private UserEntityRepository userEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));
        var replyEntities = replyEntityRepository.findByPost(postEntity);
        return replyEntities.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        var replyEntity =
                replyEntityRepository.save(ReplyEntity.of(replyPostRequestBody.body(), currentUser, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);

        return Reply.from(replyEntity);
    }
    public Reply updateReply(
            Long postId, Long replyId, ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {
        var replyEntity = replyEntityRepository
                .findById(replyId)
                .orElseThrow(
                        () -> new ReplyNotFoundException(replyId));

        if(!replyEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());
        var updateReplyEntity = replyEntityRepository.save(replyEntity);

        return Reply.from(updateReplyEntity);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository
                .findById(replyId)
                .orElseThrow(
                        () -> new ReplyNotFoundException(replyId));

        if(!replyEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        replyEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }

    public List<Reply> getRepliesByUser(String username) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var replyEntities = replyEntityRepository.findByUser(userEntity);
        return replyEntities.stream().map(Reply::from).toList();


    }
}
