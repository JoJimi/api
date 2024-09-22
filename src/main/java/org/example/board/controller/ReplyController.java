package org.example.board.controller;

import org.example.board.model.entity.UserEntity;
import org.example.board.model.post.Post;
import org.example.board.model.post.PostPatchRequestBody;
import org.example.board.model.post.PostPostRequestBody;
import org.example.board.model.reply.Reply;
import org.example.board.model.reply.ReplyPatchRequestBody;
import org.example.board.model.reply.ReplyPostRequestBody;
import org.example.board.service.PostService;
import org.example.board.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    @Autowired private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId){
        var replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId,
            @RequestBody ReplyPostRequestBody replyPostRequestBody,
            Authentication authentication){
        var reply = replyService.createReply(postId, replyPostRequestBody, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updatePost(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody replyPatchRequestBody,
            Authentication authentication){
        var reply = replyService.updateReply(postId, replyId, replyPatchRequestBody, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            Authentication authentication){
        replyService.deleteReply(postId, replyId, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }



}
