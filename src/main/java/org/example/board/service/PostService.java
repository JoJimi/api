package org.example.board.service;

import org.example.board.exception.post.PostNotFoundException;
import org.example.board.exception.user.UserNotAllowedException;
import org.example.board.exception.user.UserNotFoundException;
import org.example.board.model.entity.LikeEntity;
import org.example.board.model.entity.UserEntity;
import org.example.board.model.post.Post;
import org.example.board.model.post.PostPatchRequestBody;
import org.example.board.model.post.PostPostRequestBody;
import org.example.board.model.entity.PostEntity;
import org.example.board.repository.LikeEntityRepository;
import org.example.board.repository.PostEntityRepository;
import org.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PostService {

    @Autowired private PostEntityRepository postEntityRepository;
    @Autowired private UserEntityRepository userEntityRepository;
    @Autowired private LikeEntityRepository likeEntityRepository;

    public List<Post> getPosts(){
        var postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    public Post getPostByPostId(Long postId){
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        return Post.from(postEntity);
    }


    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currentUser) {
        var postEntity = postEntityRepository.save(
                PostEntity.of(postPostRequestBody.body(), currentUser)
        );
        return Post.from(postEntity);
    }

    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody, UserEntity currentUser) {
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        if(!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());
        var updatedPostEntity = postEntityRepository.save(postEntity);

        return Post.from(updatedPostEntity);
    }


    public void deletePost(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        if(!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostByUsername(String username) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var postEntities = postEntityRepository.findByUser(userEntity);
        return postEntities.stream().map(Post::from).toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository
                .findById(postId)
                .orElseThrow(
                        () -> new PostNotFoundException(postId));

        var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if(likeEntity.isPresent()){
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
        }else{
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
        }
        return Post.from(postEntityRepository.save(postEntity));
    }
}
