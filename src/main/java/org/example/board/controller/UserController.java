package org.example.board.controller;

import jakarta.validation.Valid;
import org.example.board.model.entity.UserEntity;
import org.example.board.model.post.Post;
import org.example.board.model.reply.Reply;
import org.example.board.model.user.*;
import org.example.board.service.PostService;
import org.example.board.service.ReplyService;
import org.example.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.apache.coyote.http11.Constants.a;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired UserService userService;
    @Autowired PostService postService;
    @Autowired ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) String query, Authentication authentication){
        var users = userService.getUsers(query, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(
            @PathVariable String username, Authentication authentication){
        var user = userService.getUser(username, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody UserPatchRequestBody requestBody,
            Authentication authentication){
        var user = userService.updateUser(username, requestBody, (UserEntity)authentication.getPrincipal() );
        return ResponseEntity.ok(user);
    }


    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostByUsername(
            @PathVariable String username, Authentication authentication){
        var posts = postService.getPostByUsername(username, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(
            @PathVariable String username, Authentication authentication){
        var user = userService.follow(username, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(
            @PathVariable String username, Authentication authentication){
        var user = userService.unFollow(username, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowersByUser(
            @PathVariable String username, Authentication authentication){
        var followers = userService.getFollowersByUsername(username, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(
            @PathVariable String username, Authentication authentication){
        var followings = userService.getFollowingsByUsername(username, (UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(
            @PathVariable String username){
        var replies = replyService.getRepliesByUser(username);
        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody){
        var user =
                userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody userLoginRequestBody){
        var response =
                userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());
        return ResponseEntity.ok(response);
    }
}
