package org.example.board.model;


import java.time.ZonedDateTime;
import java.util.Objects;

public class Post {
    private Long postId;
    private String body;
    private ZonedDateTime createDateTime;

    public Post(Long postId, String body, ZonedDateTime createDateTime) {
        this.postId = postId;
        this.body = body;
        this.createDateTime = createDateTime;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getPostId(), post.getPostId())
                && Objects.equals(getBody(), post.getBody())
                && Objects.equals(getCreateDateTime(), post.getCreateDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getBody(), getCreateDateTime());
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", body='" + body + '\'' +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
