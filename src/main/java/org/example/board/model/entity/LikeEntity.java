package org.example.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "like",
indexes = {@Index(name = "like_userid_postid_idx", columnList = "userid, postid", unique = true)})
public class LikeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column
    private ZonedDateTime createdDateTime;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private PostEntity post;

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntity that = (LikeEntity) o;
        return Objects.equals(getLikeId(), that.getLikeId()) && Objects.equals(getCreatedDateTime(), that.getCreatedDateTime()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getPost(), that.getPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLikeId(), getCreatedDateTime(), getUser(), getPost());
    }

    public static LikeEntity of(UserEntity user, PostEntity post){
        var like = new LikeEntity();
        like.setPost(post);
        like.setUser(user);
        return like;
    }

    @PrePersist
    private void prePersist(){
        this.createdDateTime = ZonedDateTime.now();
    }
}
