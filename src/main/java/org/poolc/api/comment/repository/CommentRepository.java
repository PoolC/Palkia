package org.poolc.api.comment.repository;

import org.poolc.api.comment.domain.Comment;
import org.poolc.api.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT DISTINCT c FROM Comment c WHERE c.post = :post")
    List<Comment> findAllByPost(@Param("post") Post post);

    @Query(value = "SELECT DISTINCT c FROM Comment c WHERE c.parent = :parent")
    List<Comment> findAllByParent(@Param("parent") Comment parent);
}
