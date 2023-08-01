package org.poolc.api.comment.repository;

import org.poolc.api.comment.domain.Reply;
import org.poolc.api.post.domain.QuestionPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query(value = "SELECT DISTINCT r FROM Reply r WHERE r.questionPost = :questionPost")
    List<Reply> findAllByQuestionPost(QuestionPost questionPost);
}
